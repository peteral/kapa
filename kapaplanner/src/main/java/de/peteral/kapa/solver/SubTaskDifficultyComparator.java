package de.peteral.kapa.solver;

import de.peteral.kapa.domain.SubTask;

import java.util.Comparator;

public class SubTaskDifficultyComparator implements Comparator<SubTask> {

    @Override
    public int compare(SubTask o1, SubTask o2) {
        if (o1.getTask().getProject().getDue() == null && o2.getTask().getProject().getDue() == null)
            return 0;

        if (o1.getTask().getProject().getDue() == null)
            return Integer.MAX_VALUE;

        if (o2.getTask().getProject().getDue() == null)
            return Integer.MIN_VALUE;

        int result = o1.getTask().getProject().getDue().compareTo(o2.getTask().getProject().getDue());

        if (result != 0)
            return result;

        return Integer.compare(o2.getTask().getProject().getCostsOfDelay(), o1.getTask().getProject().getCostsOfDelay());
    }
}
