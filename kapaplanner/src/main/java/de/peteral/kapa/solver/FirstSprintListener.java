package de.peteral.kapa.solver;

import de.peteral.kapa.domain.Sprint;
import de.peteral.kapa.domain.SubTask;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FirstSprintListener extends AbstractVariableListener<SubTask> {
    @Override
    protected void updateProperty(ScoreDirector scoreDirector, SubTask source) {
        Optional<Sprint> sprint = source.getTask().getSubtasks().stream()
                .filter(subTask -> subTask.getSprint() != null)
                .map(SubTask::getSprint)
                .distinct()
                .collect(Collectors.minBy(Comparator.comparing(Sprint::getName)));

        if (source.getTask().getFirstSprint() != sprint.orElse(null)) {
            scoreDirector.beforeVariableChanged(source.getTask(), "firstSprint");
            source.getTask().setFirstSprint(sprint.orElse(null));
            scoreDirector.afterVariableChanged(source.getTask(), "firstSprint");
        }
    }
}
