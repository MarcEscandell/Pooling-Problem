package com.petronas.opt.service.provider;

import com.petronas.opt.compiler.pooling_problem.OptimizationRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PoolingProblemEntryInterface {

    @RequestMapping("/execute-opt")
    public String executeOpt() {
        OptimizationRunner.run();
        return "Optimization execution has been completed!\n";
    }

}