package de.peteral.kapa.solver;

import de.peteral.kapa.domain.Sprint;

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

    public static int getUtilizationMalus(Sprint sprint, long work) {
        long unutilized = sprint.getVelocity() - work;
        return - (int) ((sprints.size() - sprints.indexOf(sprint.getName())) * unutilized);
    }
}
