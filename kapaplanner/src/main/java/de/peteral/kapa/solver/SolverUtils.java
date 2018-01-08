package de.peteral.kapa.solver;

import java.util.Collections;
import java.util.List;

public class SolverUtils {
    private static List<String> sprints = Collections.emptyList();

    public static int getSprintDifference(String s1, String s2) {
        return sprints.indexOf(s1) - sprints.indexOf(s2);
    }

    public static int getSprintIndex(String sprint) {
        return sprints.indexOf(sprint);
    }

    public static int getSprintCount() {
        return sprints.size();
    }

    public static void injectSprints(List<String> sprints) {
        SolverUtils.sprints = sprints;
    }
}
