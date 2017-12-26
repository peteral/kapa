package de.peteral.kapa.solver;

import de.peteral.kapa.domain.Sprint;
import de.peteral.kapa.domain.SubTask;
import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskMaxVelocityListener extends AbstractVariableListener<SubTask> {
    @Override
    protected void updateProperty(ScoreDirector scoreDirector, SubTask sourceSubTask) {
        if (sourceSubTask.getTask().getMaxVelocity() > 0) {
            boolean violationFound = false;
            Map<Sprint, List<SubTask>> subTasksBySprint = sourceSubTask.getTask().getSubtasks().stream()
                    .filter(subTask -> subTask.getSprint() != null)
                    .collect(Collectors.groupingBy(SubTask::getSprint));
            for (Map.Entry<Sprint, List<SubTask>> entry : subTasksBySprint.entrySet()) {
                if (entry.getValue().stream().collect(Collectors.summarizingLong(SubTask::getWork))
                        .getSum() > sourceSubTask.getTask().getMaxVelocity()) {
                    violationFound = true;
                    break;
                }
            }

            if (violationFound != sourceSubTask.getTask().getSprintViolatesMaxVelocity()) {

                scoreDirector.beforeVariableChanged(sourceSubTask.getTask(), "sprintViolatesMaxVelocity");
                sourceSubTask.getTask().setSprintViolatesMaxVelocity(violationFound);
                scoreDirector.afterVariableChanged(sourceSubTask.getTask(), "sprintViolatesMaxVelocity");
            }
        }
    }
}
