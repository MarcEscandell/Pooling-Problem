package com.petronas.pooling_problem;
import com.google.ortools.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class PoolingProblemExample1 {
        /**
         * Run test model
         */
        public static void run(PiecewiseRelaxationScheme scheme) {
            MPSolver solver = new MPSolver("Pooling problem example 1", scheme.getOptProblemType());
            // Initialise all variables manually
            double infinity = java.lang.Double.POSITIVE_INFINITY;
            MPVariable d = solver.makeNumVar(0, 10000, "D");
            MPVariable r = solver.makeNumVar(100, infinity, "R");
            MPVariable rd = solver.makeNumVar(0, infinity, "RD");
            MPVariable rf = solver.makeNumVar(0, infinity, "RF");
            MPVariable m = solver.makeNumVar(100, infinity, "M");
            MPVariable md = solver.makeNumVar(0, infinity, "MD");
            MPVariable mf = solver.makeNumVar(0, infinity, "MF");
            MPVariable p = solver.makeNumVar(100, infinity, "P");
            MPVariable pd = solver.makeNumVar(0, infinity, "PD");
            MPVariable pf = solver.makeNumVar(0, infinity, "PF");
            MPVariable a = solver.makeNumVar(0, 50, "A");
            MPVariable s1 = solver.makeNumVar(0, infinity, "S1");
            MPVariable s2 = solver.makeNumVar(0, infinity, "S2");
            MPVariable x = solver.makeNumVar(93, 97, "X");
            // Initialise constraints
            // R = RD + RF
            MPConstraint rCons = solver.makeConstraint(0, 0, "R_EQUALS_RD_PLUS_RF");
            rCons.setCoefficient(r, 1.0);
            rCons.setCoefficient(rd, -1.0);
            rCons.setCoefficient(rf, -1.0);
            // M = MD + MF
            MPConstraint mCons = solver.makeConstraint(0, 0, "M_EQUALS_MD_PLUS_MF");
            mCons.setCoefficient(m, 1.0);
            mCons.setCoefficient(md, -1.0);
            mCons.setCoefficient(mf, -1.0);
            // P = PD + PF + A
            MPConstraint pCons = solver.makeConstraint(0, 0, "P_EQUALS_PD_PLUS_PF_PLUS_A");
            pCons.setCoefficient(p, 1.0);
            pCons.setCoefficient(pd, -1.0);
            pCons.setCoefficient(pf, -1.0);
            pCons.setCoefficient(a, -1.0);
            // D = RD + MD + PD
            MPConstraint dCons = solver.makeConstraint(0, 0, "D_EQUALS_RD_PLUS_MD_PLUS_PD");
            dCons.setCoefficient(d, 1.0);
            dCons.setCoefficient(rd, -1.0);
            dCons.setCoefficient(md, -1.0);
            dCons.setCoefficient(pd, -1.0);
            // RF + MF + PF = S1 + S2
            MPConstraint fCons = solver.makeConstraint(0, 0, "RF_PLUS_MF_PLUS_PF_EQUALS_S1_PLUS_S2");
            fCons.setCoefficient(rf, 1.0);
            fCons.setCoefficient(mf, 1.0);
            fCons.setCoefficient(pf, 1.0);
            fCons.setCoefficient(s1, -1.0);
            fCons.setCoefficient(s2, -1.0);

            // Initialise linear relaxation details
            LinearRelaxationDetails.Builder lrDetailsBuilder = new LinearRelaxationDetails.Builder(scheme);
            lrDetailsBuilder.setVariableX(x);
            lrDetailsBuilder.setBoundX(95, 97);
            // 85RD + xRF > 87R
            // Continue with bilinear constraints
            MPConstraint rRONCons = solver.makeConstraint(0.0, infinity, "85RD_PLUS_xRF_GREATER_THAN_87R");
            lrDetailsBuilder.setVariableY(rf);
            lrDetailsBuilder.setBoundY(0, 10000);
            LinearRelaxation linearRelaxDetailsRF = getLinearRelaxDetails(lrDetailsBuilder);
            rRONCons.setCoefficient(linearRelaxDetailsRF.applyLinearRelaxation(solver, "X_x_RF"), 1);
            rRONCons.setCoefficient(rd, 85.0);
            rRONCons.setCoefficient(r, -87.0);
            // 85MD + xMF > 89M
            MPConstraint mRONCons = solver.makeConstraint(0.0, infinity, "85MD_PLUS_xMF_GREATER_THAN_89M");
            lrDetailsBuilder.setVariableY(mf);
            lrDetailsBuilder.setBoundY(0, 10000);
            LinearRelaxation linearRelaxDetailsMF = getLinearRelaxDetails(lrDetailsBuilder);
            mRONCons.setCoefficient(linearRelaxDetailsMF.applyLinearRelaxation(solver, "X_x_MF"), 1);
            mRONCons.setCoefficient(md, 85.0);
            mRONCons.setCoefficient(m, -89.0);
            // 85PD + xPF + 900A > 94P
            MPConstraint pRONCons = solver.makeConstraint(0.0, infinity, "85PD_PLUS_xPF_PLUS_900A_GREATER_THAN_94P");
            lrDetailsBuilder.setVariableY(pf);
            lrDetailsBuilder.setBoundY(0, 10000);
            LinearRelaxation linearRelaxDetailsPF = getLinearRelaxDetails(lrDetailsBuilder);
            pRONCons.setCoefficient(linearRelaxDetailsPF.applyLinearRelaxation(solver, "X_x_PF"), 1);
            pRONCons.setCoefficient(pd, 85.0);
            pRONCons.setCoefficient(a, 900.0);
            pRONCons.setCoefficient(p, -94.0);
            // X(S1 + S2) = 93S1 + 97S2
            MPConstraint sRONCons = solver.makeConstraint(0.0, 0.0, "X_MULTIPLY_BY_S1_PLUS_S2_EQUALS_93S1_PLUS_97S2");
            lrDetailsBuilder.setVariableY(s1);
            lrDetailsBuilder.setBoundY(0, 10000);
            LinearRelaxation linearRelaxDetailsS1 = getLinearRelaxDetails(lrDetailsBuilder);
            sRONCons.setCoefficient(linearRelaxDetailsS1.applyLinearRelaxation(solver, "S2_x_PF"), 1);
            lrDetailsBuilder.setVariableY(s2);
            lrDetailsBuilder.setBoundY(0, 10000);
            LinearRelaxation linearRelaxDetailsS2 = getLinearRelaxDetails(lrDetailsBuilder);
            sRONCons.setCoefficient(linearRelaxDetailsS2.applyLinearRelaxation(solver, "S1_x_PF"), 1);
            sRONCons.setCoefficient(s1, -93);
            sRONCons.setCoefficient(s2, -97);
            // S1 + S2 < 8000
            MPConstraint sDemand = solver.makeConstraint(0, 8000, "S1_PLUS_S2_SMALLER_THAN_8000");
            sDemand.setCoefficient(s1, 1);
            sDemand.setCoefficient(s2, 1);

            // Set objective function
            MPObjective objective = solver.objective();
            objective.setCoefficient(r, 1.18);
            objective.setCoefficient(m, 1.25);
            objective.setCoefficient(p, 1.4);
            objective.setCoefficient(s1, -0.8);
            objective.setCoefficient(s2, -0.9);
            objective.setCoefficient(d, -0.65);
            objective.setCoefficient(a, -30);

            objective.setMaximization();

            System.out.println(solver.exportModelAsLpFormat());

            // Solve optimisation
            solver.solve();

            // Pring optimal values
            System.out.println("RESULTS:");
            for (MPVariable variable: solver.variables()) {
                System.out.println(variable.name() + " = " + variable.solutionValue());
            }
            System.out.println("\n-> Objective = " + solver.objective().value());
        }

        /**
         * Create and initialise linear relaxation details
         * @param lrDetailsBuilder
         * @return
         */
        public static LinearRelaxation getLinearRelaxDetails(LinearRelaxationDetails.Builder lrDetailsBuilder) {
            switch (lrDetailsBuilder.getScheme()) {
                case MC_CORMICK_ENVELOPES:
                    return new McCormickEnvDetails(lrDetailsBuilder);
                case BIG_M:
                case BIG_M_NF2G:
                    BigMDetails bigMDetails = new BigMDetails(lrDetailsBuilder);
                    return bigMDetails
                            .setNumberOfPartitions(BigMDetails.DEFAULT_PARTITIONS)
                            .setGammaForPartitions(BigMDetails.DEFAULT_GAMMA);
                default:
                    throw new AssertionError(lrDetailsBuilder.getScheme() + " has not been implemented");
            }
        }

        @Override
        public String toString() {
            return super.toString();
        }
}
