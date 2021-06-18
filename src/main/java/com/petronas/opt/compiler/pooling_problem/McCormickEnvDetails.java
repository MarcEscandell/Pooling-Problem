package com.petronas.opt.compiler.pooling_problem;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

/**
 * McCormickEnvDetails
 */
public class McCormickEnvDetails extends LinearRelaxationDetails implements LinearRelaxation {

    /**
     * Default constructor using linear relaxation common details
     */
    public McCormickEnvDetails(Builder lrDetailsBuilder) {
        super(lrDetailsBuilder);
    }

    /**
     * Apply linear relaxation
     */
    public MPVariable applyLinearRelaxation(MPSolver solver, String bilinearTermExpression) {
        // Initialise linear term
        MPVariable z = solver.makeNumVar(0, infinity, "Z_FOR_" + bilinearTermExpression);
        // Construct McCormick envelopes
        // Envelope 1
        MPConstraint env1 = solver.makeConstraint(-infinity, xLower * yLower, "ENV1_" + bilinearTermExpression);
        env1.setCoefficient(z, -1);
        env1.setCoefficient(x, yLower);
        env1.setCoefficient(y, xLower);
        // Envelope 2
        MPConstraint env2 = solver.makeConstraint(-infinity, xUpper * yUpper, "ENV2_" + bilinearTermExpression);
        env2.setCoefficient(z, -1);
        env2.setCoefficient(x, yUpper);
        env2.setCoefficient(y, xUpper);
        // Envelope 3
        MPConstraint env3 = solver.makeConstraint(xLower * yUpper, infinity, "ENV3_" + bilinearTermExpression);
        env3.setCoefficient(z, -1);
        env3.setCoefficient(x, yUpper);
        env3.setCoefficient(y, xLower);
        // Envelope 4
        MPConstraint env4 = solver.makeConstraint(xUpper * yLower, infinity, "ENV4_" + bilinearTermExpression);
        env4.setCoefficient(z, -1);
        env4.setCoefficient(x, yLower);
        env4.setCoefficient(y, xUpper);
        // Return linear term
        return z;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
