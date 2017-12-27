package de.peteral.kapa.solver;

import java.util.Arrays;
import java.util.List;

public class SolverUtils {
        private static final List<String> SPRINTS = Arrays.asList(new String[]{
            "7.20","7.21","7.22","7.23","7.24","7.25","7.26","7.27","7.28","7.29","7.30"
    });

    public static int getDelayCosts(String due, String lastSprint, int costsPerSprint) {
        return (SPRINTS.indexOf(lastSprint) - SPRINTS.indexOf(due)) * costsPerSprint;
    }
}
