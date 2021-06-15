package com.petronas.pooling_problem;

import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import com.google.ortools.linearsolver.MPConstraint;

import java.util.ArrayList;

/**
 * BigMDetails
 */
public class BigMDetails extends LinearRelaxationDetails implements LinearRelaxation {

    private ArrayList<MPVariable> lambdas = new ArrayList<>();
    private int N;
    private double M;
    private double gamma;
    // Parameters for dx partitioning deviation MF2G
    private MPConstraint equation23_1;
    private MPConstraint equation23_2;
    private MPVariable deltaX;

    public static final int DEFAULT_PARTITIONS = 2;
    public static final int DEFAULT_GAMMA = 1;

    /**
     * Default constructor using linear relaxation common details
     */
    public BigMDetails(LinearRelaxationDetails.Builder lrDetaBuilder) {
        super(lrDetaBuilder);
        // Define smallest possible M coefficient
        M = (xUpper - xLower) * (yUpper - yLower);
    }

    /**
     * Set number of partitions n
     * @param n
     * @return
     */
    public BigMDetails setNumberOfPartitions(int N) {
        this.N = N;
        return this;
    }

    /**
     * Set gamma for parititons approach
     * @param gamma
     * @return
     */
    public BigMDetails setGammaForPartitions(double gamma) {
        this.gamma = gamma;
        return this;
    }

    /**
     * Get partition Xn
     * @param n
     * @return
     */
    private double getPartitionXn(int n) {
        return xLower + Math.pow((double) n / N, gamma) * (xUpper - xLower);
    }

    /**
     * Get domain dx length as [x(n) - x(n-1)]
     * @param n
     * @return
     */
    private double getDomainDx(int n) {
        return (Math.pow((double) n / N, gamma) - Math.pow((double) (n - 1) / N, gamma)) * (xUpper - xLower);
    }

    /**
     * Apply linear relaxation
     */
    public MPVariable applyLinearRelaxation(MPSolver solver, String bilinearTermExpression) {
        // Initialise linear term
        MPVariable z = solver.makeNumVar(0, infinity, "Z_" + bilinearTermExpression);
        // Implementation as defined by 'Gounaris_Misener_Floudas_2009_Comparison_Piecewise−Linear Relaxations_Pooling' paper
        // It will be implementing equations (21), (29), (30)
        // Initialise equality (21)
        MPConstraint lambdaEq = solver.makeConstraint(1, 1, bilinearTermExpression + "_ALL_LAMBDAS_SUMMATION");
        for (int nIndex = 1; nIndex <= N; nIndex++) {
            // Initialise all lambdas for each N subdomain as defined by
            MPVariable lambdaN = solver.makeBoolVar(bilinearTermExpression + "_LAMBDA_n" + nIndex);
            lambdas.add(lambdaN);
            // Terms refering to equation (21) as all lambdas must add up to 1
            // Since variable x always lies in one of the N subdomains, eq
            // 21 is included in the formulations that involve the λ variables
            // in order to provide coherence and tighten the relaxation
            lambdaEq.setCoefficient(lambdaN, 1.0);
            // Terms refering to equation (29)
            // Implement Dx partitioning
            dxPartitioning(nIndex, solver, bilinearTermExpression);
            // Terms refering to equation (30)
            // Equation 1
            MPConstraint eq30_1 = solver.makeConstraint(-infinity, M + getPartitionXn(nIndex - 1) * yLower, bilinearTermExpression + "_EQ30_1_N_" + nIndex);
            eq30_1.setCoefficient(z, -1);
            eq30_1.setCoefficient(x, yLower);
            eq30_1.setCoefficient(y, getPartitionXn(nIndex - 1));
            eq30_1.setCoefficient(lambdaN, M);
            // Equation 2
            MPConstraint eq30_2 = solver.makeConstraint(-infinity, M + getPartitionXn(nIndex) * yUpper, bilinearTermExpression + "_EQ30_2_N_" + nIndex);
            eq30_2.setCoefficient(z, -1);
            eq30_2.setCoefficient(x, yUpper);
            eq30_2.setCoefficient(y, getPartitionXn(nIndex));
            eq30_2.setCoefficient(lambdaN, M);
            // Equation 3
            MPConstraint eq30_3 = solver.makeConstraint(-infinity, M - getPartitionXn(nIndex - 1) * yUpper, bilinearTermExpression + "_EQ30_3_N_" + nIndex);
            eq30_3.setCoefficient(z, 1);
            eq30_3.setCoefficient(x, -yUpper);
            eq30_3.setCoefficient(y, -getPartitionXn(nIndex - 1));
            eq30_3.setCoefficient(lambdaN, M);
            // Equation 4
            MPConstraint eq30_4 = solver.makeConstraint(-infinity, M - getPartitionXn(nIndex) * yLower, bilinearTermExpression + "_EQ30_4_N_" + nIndex);
            eq30_4.setCoefficient(z, 1);
            eq30_4.setCoefficient(x, -yLower);
            eq30_4.setCoefficient(y, -getPartitionXn(nIndex));
            eq30_4.setCoefficient(lambdaN, M);
        }
        // Return linear term
        return z;
    }
    /**
     * In the original approach, the partitioning of domain Dx is
     * modeled according to eqs 29. For the subdomain for which λn = 1,
     * these constraints tighten the bounds of variable x
     * appropriately, while for all other subdomains these constraints
     * become redundant as they collapse to the overall hard bounds of x.
     */
    public void dxPartitioning(int nIndex, MPSolver solver, String bilinearTermExpression) {
        // Switch between different partitioning types
        switch (getScheme()) {
            case BIG_M:
                // Create and define Dx partitioning as per (29)
                MPConstraint eq29_1 = solver.makeConstraint(xLower, infinity, bilinearTermExpression + "_EQ29_1_N_" + nIndex);
                eq29_1.setCoefficient(x, 1);
                eq29_1.setCoefficient(lambdas.get(nIndex - 1), xLower - getPartitionXn(nIndex - 1));
                // Second constraint as per (29)
                MPConstraint eq29_2 = solver.makeConstraint(-infinity, xUpper, bilinearTermExpression + "_EQ29_2_N_" + nIndex);
                eq29_2.setCoefficient(x, 1);
                eq29_2.setCoefficient(lambdas.get(nIndex - 1), getPartitionXn(nIndex) - xUpper);
                break;
            case BIG_M_NF2G:
                MPConstraint eq23_1 = getEquation23_1(solver, bilinearTermExpression);
                eq23_1.setCoefficient(lambdas.get(nIndex - 1), getPartitionXn(nIndex - 1));
                MPConstraint eq23_2 = getEquation23_2(solver, bilinearTermExpression);
                // Set d(n) λ(n)
                equation23_2.setCoefficient(lambdas.get(nIndex - 1), getDomainDx(nIndex));
                break;
            default:
                throw new AssertionError(getScheme() + " is not a valid deviation for Dx partitioning Big-M scheme");
        }
    }

    /**
     * Get equation 23 (1) for derivation MF2G
     * @param solver
     * @param bilinearTermExpression
     * @return
     */
    private MPConstraint getEquation23_1(MPSolver solver, String bilinearTermExpression) {
        // This can only be requested by scheme MF2G
        assert getScheme() == PiecewiseRelaxationScheme.BIG_M_NF2G: "It shouldn't be using eq (23) for scheme " + getScheme();
        // Check if equality has already been initialised, otherwise create
        if (equation23_1 == null) {
            equation23_1 = solver.makeConstraint(0, 0, bilinearTermExpression + "_EQ23_1");
            // Set X coefficient
            equation23_1.setCoefficient(x, -1);
            // Create ∆x and add to equality
            deltaX = solver.makeNumVar(0, infinity, bilinearTermExpression + "_DELTA_X");
            equation23_1.setCoefficient(deltaX, 1);
        }
        return equation23_1;
    }

    /**
     * Get equation 23 (2) for derivation MF2G
     * @param solver
     * @param bilinearTermExpression
     * @return
     */
    private MPConstraint getEquation23_2(MPSolver solver, String bilinearTermExpression) {
        // This can only be requested by scheme MF2G
        assert getScheme() == PiecewiseRelaxationScheme.BIG_M_NF2G: "It shouldn't be using eq (23) for scheme " + getScheme();
        // Check if equality has already been initialised, otherwise create
        if (equation23_2 == null) {
            equation23_2 = solver.makeConstraint(0, infinity, bilinearTermExpression + "_EQ23_2");
            // Set ∆x coefficient
            equation23_2.setCoefficient(deltaX, -1);
        }
        return equation23_2;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
