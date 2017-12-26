package de.peteral.kapa.view;

import de.peteral.kapa.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class Visualization {
    private final Map<Team, Map<Sprint, Map<Task, List<SubTask>>>> data;

    public Visualization(Schedule schedule) {
        data = new HashMap<>();

        schedule.getSubTasks().forEach(subTask -> {
            Map<Sprint, Map<Task, List<SubTask>>> teamSprints = data.get(subTask.getSprint().getTeam());
            if (teamSprints == null) {
                teamSprints = new HashMap<>();
                data.put(subTask.getSprint().getTeam(), teamSprints);
            }
            Map<Task, List<SubTask>> tasks = teamSprints.get(subTask.getSprint());
            if (tasks == null) {
                tasks = new HashMap<>();
                teamSprints.put(subTask.getSprint(), tasks);
            }
            List<SubTask> subTasks = tasks.get(subTask.getTask());
            if (subTasks == null) {
                subTasks = new ArrayList<>();
                tasks.put(subTask.getTask(), subTasks);
            }

            subTasks.add(subTask);
        });
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        data.forEach((team, sprints) -> {
            result.append("\n").append(team);
            sprints.forEach((sprint, tasks) -> {
                result.append("\n\t").append(sprint);
                tasks.forEach((task, subTasks) ->
                    result.append("\n\t\t").append(task).append("/").append(
                            subTasks.stream().collect(Collectors.summarizingLong(SubTask::getWork)).getSum()
                    )
                );
            });
        });

        return result.toString();
    }
}
