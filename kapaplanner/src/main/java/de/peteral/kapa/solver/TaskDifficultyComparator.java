package de.peteral.kapa.solver;

import de.peteral.kapa.domain.Task;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.Comparator;

public class TaskDifficultyComparator implements Comparator<Task> {

    public int compare(Task a, Task b) {
        return new CompareToBuilder()
                .append(a.getWork(), b.getWork())
                .toComparison();
    }
}
