package com.petronas.pooling_problem;
import com.google.ortools.linearsolver.MPSolver.OptimizationProblemType;

/**
 * PiecewiseRelaxationScheme
 */
public enum PiecewiseRelaxationScheme {
    MC_CORMICK_ENVELOPES(OptimizationProblemType.GLOP_LINEAR_PROGRAMMING),
    BIG_M(OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING),
    BIG_M_NF2G(OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING),
    CONVEX_COMBINATION(OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING),
    INCREMENTAL_COST(OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);

    private final OptimizationProblemType optProblemType;
    /**
     * Default constructor
     */
    private PiecewiseRelaxationScheme(OptimizationProblemType optProblemType) {
        this.optProblemType = optProblemType;
    }

    /**
     * Get optimization problem type
     */
    public OptimizationProblemType getOptProblemType() {
        return optProblemType;
    }
}
