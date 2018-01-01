package de.peteral.kapa.solver;

import java.util.Collections;
import java.util.List;

public class SolverUtils {
        private static List<String> sprints = Collections.emptyList();

    public static int getDelayCosts(String due, String lastSprint, int costsPerSprint) {
        int result = (sprints.indexOf(lastSprint) - sprints.indexOf(due)) * costsPerSprint;
        // there is no "bonus" for finishing too early
        return (result > 0) ? result : 0;
    }

    public static void injectSprints(List<String> sprints) {
        SolverUtils.sprints = sprints;
    }
}
