package com.petronas.pooling_problem;

import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public interface LinearRelaxation {
    public MPVariable applyLinearRelaxation(MPSolver solver, String bilinearTermExpression);
}