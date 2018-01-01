package de.peteral.kapa.solver;

import de.peteral.kapa.domain.SubTask;

import java.util.Comparator;

public class SubTaskDifficultyComparator implements Comparator<SubTask> {

    @Override
    public int compare(SubTask o1, SubTask o2) {
        if (o1.getTask() == o2.getTask())
            return 0;

        if (o1.getTask().isBlockedBy(o2.getTask()))
            return Integer.MIN_VALUE;

        if (o1.getTask().blocks(o2.getTask()))
            return Integer.MAX_VALUE;

        if (o2.getTask().isBlockedBy(o1.getTask()))
            return Integer.MAX_VALUE;

        if (o2.getTask().blocks(o1.getTask()))
            return Integer.MIN_VALUE;

        // reverse dependency map might help here

        if (o1.getTask().getProject().getDue() == null && o2.getTask().getProject().getDue() == null)
            return 0;

        if (o2.getTask().getProject().getDue() == null)
            return Integer.MAX_VALUE;

        if (o1.getTask().getProject().getDue() == null)
            return Integer.MIN_VALUE;

        int result = o2.getTask().getProject().getDue().compareTo(o1.getTask().getProject().getDue());

        if (result != 0)
            return result;

        return Integer.compare(o1.getTask().getProject().getCostsOfDelay(), o2.getTask().getProject().getCostsOfDelay());
    }
}
