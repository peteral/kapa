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
        // FIXME we are relying on ascending ordering of sprint number id's in configuration data here - this should be changed
        Optional<Sprint> sprint = source.getTask().getSubtasks().stream()
                .filter(subTask -> subTask.getSprint() != null)
                .map(SubTask::getSprint)
                .distinct()
                .collect(Collectors.maxBy(Comparator.comparing(Sprint::getId)));

        if (source.getTask().getLastSprint() != sprint.orElse(null)) {
            scoreDirector.beforeVariableChanged(source.getTask(), "lastSprint");
            source.getTask().setLastSprint(sprint.orElse(null));
            scoreDirector.afterVariableChanged(source.getTask(), "lastSprint");
        }

    }
}
