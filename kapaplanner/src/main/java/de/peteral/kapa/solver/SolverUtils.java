package de.peteral.kapa.solver;

import java.util.Collections;
import java.util.List;

public class SolverUtils {
        private static List<String> sprints = Collections.emptyList();

    public static int getDelayCosts(String due, String lastSprint, int costsPerSprint) {
        return (sprints.indexOf(lastSprint) - sprints.indexOf(due)) * costsPerSprint;
    }

    public static void injectSprints(List<String> sprints) {
        SolverUtils.sprints = sprints;
    }
}
