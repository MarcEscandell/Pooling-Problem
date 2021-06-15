package com.petronas.pooling_problem;

import com.google.ortools.Loader;
import java.util.concurrent.TimeUnit;

public class OptimizationRunner {

    public static void main(String[] args) {
        Loader.loadNativeLibraries();
        System.out.println("Started optimization...");
        // Save start time
        long startTime = System.currentTimeMillis();
        // Run testing
        PoolingProblemExample1.run(PiecewiseRelaxationScheme.BIG_M_NF2G);
        // Calculate execution time
        long executionTime = System.currentTimeMillis() - startTime;
        // Format execution time
        System.out.println("> Execution time (mm:ss) was "
                + String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(executionTime),
                TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime)))
                + " <");
    }
    
}
