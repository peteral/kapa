package de.peteral.kapa.solver;

import de.peteral.kapa.domain.Sprint;
import de.peteral.kapa.domain.SubTask;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class LastSprintListener extends AbstractVariableListener<SubTask> {
    @Override
    protected void updateProperty(ScoreDirector scoreDirector, SubTask source) {
        Optional<Sprint> sprint = source.getTask().getSubtasks().stream()
                .filter(subTask -> subTask.getSprint() != null)
                .map(SubTask::getSprint)
                .distinct()
                .collect(Collectors.maxBy(Comparator.comparing(Sprint::getName)));

        if (source.getTask().getLastSprint() != sprint.orElse(null)) {
            scoreDirector.beforeVariableChanged(source.getTask(), "lastSprint");
            source.getTask().setLastSprint(sprint.orElse(null));
            scoreDirector.afterVariableChanged(source.getTask(), "lastSprint");
        }

    }
}
