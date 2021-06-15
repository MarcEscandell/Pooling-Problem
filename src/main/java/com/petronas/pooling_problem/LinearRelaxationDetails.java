package com.petronas.pooling_problem;

import com.google.ortools.linearsolver.MPVariable;
import com.google.ortools.linearsolver.MPSolver;

/**
 * com.petronas.pooling_problem.LinearRelaxationDetails
 * Abstract class for linear relaxation approach details
 * @author marc.escandellmari
 */
public class LinearRelaxationDetails implements LinearRelaxation {

    public static final double infinity = java.lang.Double.POSITIVE_INFINITY;
    public final PiecewiseRelaxationScheme scheme;
    public final MPVariable x;
    public final MPVariable y;
    public final double xLower;
    public final double xUpper;
    public final double yLower;
    public final double yUpper;

    /**
     * Common builder for all pooling approaches
     * @author marcescandell
     *
     */
    public static class Builder {

        private final PiecewiseRelaxationScheme scheme;
        private MPVariable x;
        private MPVariable y;
        private double xLower;
        private double xUpper;
        private double yLower;
        private double yUpper;

        /**
         * Default constructor initialising scheme
         */
        public Builder(PiecewiseRelaxationScheme scheme) {
            this.scheme = scheme;
        }

        /**
         * Get scheme
         * @return
         */
        public PiecewiseRelaxationScheme getScheme() {
            return scheme;
        }

        /**
         * Initialise x variable for biliniear term
         * @param x
         * @return
         */
        public Builder setVariableX(MPVariable x) {
            this.x = x;
            return this;
        }

        /**
         * Initialise y variable for bilinear term
         * @param y
         * @return
         */
        public Builder setVariableY(MPVariable y) {
            this.y = y;
            return this;
        }

        /**
         * Set x variable lower and upper bounds
         * @param xLower
         * @param xUpper
         * @return
         */
        public Builder setBoundX(double xLower, double xUpper) {
            this.xLower = xLower;
            this.xUpper = xUpper;
            return this;
        }

        /**
         * Set y variable lower and upper bounds
         * @param yLower
         * @param yUpper
         * @return
         */
        public Builder setBoundY(double yLower, double yUpper) {
            this.yLower = yLower;
            this.yUpper = yUpper;
            return this;
        }
    }

    /**
     * Default constructor
     */
    public LinearRelaxationDetails(Builder lrDetailsBuilder) {
        this.scheme = lrDetailsBuilder.scheme;
        this.x = lrDetailsBuilder.x;
        this.y = lrDetailsBuilder.y;
        this.xLower = lrDetailsBuilder.xLower;
        this.xUpper = lrDetailsBuilder.xUpper;
        this.yLower = lrDetailsBuilder.yLower;
        this.yUpper = lrDetailsBuilder.yUpper;
    }

    /**
     * Get piecewise relaxation scheme
     */
    public PiecewiseRelaxationScheme getScheme() {
        return scheme;
    }

    /**
     * Apply linear relaxation
     */
    public MPVariable applyLinearRelaxation(MPSolver solver, String bilinearTermName) {
        throw new AssertionError("Linear relaxation schema must be implemented for " + scheme);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * This number is here for model snapshot storing purpose<br>
     * It needs to be changed when this class gets changed
     */
    private static final long serialVersionUID = 1L;

}