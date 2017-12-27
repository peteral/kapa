package de.peteral.kapa.solver;

import de.peteral.kapa.domain.Project;
import de.peteral.kapa.domain.Sprint;
import de.peteral.kapa.domain.Task;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectLastSprintListener extends AbstractVariableListener<Task> {
    @Override
    protected void updateProperty(ScoreDirector scoreDirector, Task sourceTask) {
        Project project = sourceTask.getProject();

        Optional<Sprint> lastSprint = project.getTasks().stream()
                .filter(task -> task.getLastSprint() != null)
                .map(Task::getLastSprint)
                .distinct()
                .collect(Collectors.maxBy(Comparator.comparing(Sprint::getName)));

        if (lastSprint.isPresent() && !lastSprint.equals(Optional.ofNullable(project.getLastSprint()))) {
            scoreDirector.beforeVariableChanged(project, "lastSprint");
            project.setLastSprint(lastSprint.get());
            scoreDirector.afterVariableChanged(project, "lastSprint");
        }
    }
}
