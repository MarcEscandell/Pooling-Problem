package com.petronas.opt.compiler.pooling_problem;

import com.google.ortools.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class OptimizationRunner {

    public static final Logger logger = LoggerFactory.getLogger(OptimizationRunner.class);

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        // This is a very important step to load all the system packages that allow
        // OR-Tools to solve the optimization models
        Loader.loadNativeLibraries();
        logger.info("Started running optimization algorithm...");
        // Save start time
        long startTime = System.currentTimeMillis();
        // Run optimization algorithm
        com.petronas.opt.compiler.pooling_problem.PoolingProblemExample1.run(PiecewiseRelaxationScheme.BIG_M_NF2G);
        // Calculate execution time
        long executionTime = System.currentTimeMillis() - startTime;
        // Format execution time
        logger.info("\n"
            + "----------------------------------------------------\n"
            + "                    PERFORMANCE                     \n"
            + "----------------------------------------------------");
        logger.info("Optimization execution time (mm:ss) was "
                + String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(executionTime),
                TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))));
    }
    
}
