package de.peteral.kapa.view;

import de.peteral.kapa.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class Visualization {
    private final Map<Team, Map<Sprint, Map<Task, List<SubTask>>>> data;
    private final List<Project> projects;

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

        projects = schedule.getTasks().stream().map(task -> task.getProject()).distinct().collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder()
            .append("Backlogs:\n")
            .append("-----------------------------------")
        ;

        data.forEach((team, sprints) -> {
            result.append("\n").append(team.getLabel());
            sprints.forEach((sprint, tasks) -> {
                result.append("\n\t").append(sprint.getLabel());

                result.append(
                    tasks.entrySet().stream().map(entry ->
                        entry.getValue().stream().filter(subTask -> subTask.getSprint() == sprint)
                                .collect(Collectors.summarizingLong(SubTask::getWork)).getSum())
                            .collect(Collectors.summarizingLong(Long::longValue)).getSum());

                tasks.forEach((task, subTasks) ->
                    result.append("\n\t\t").append(task.getLabel()).append("/").append(
                            subTasks.stream().collect(Collectors.summarizingLong(SubTask::getWork)).getSum()
                    )
                );
            });
        });

        result.append("\n\nProjects:\n")
            .append("-----------------------------------");

        projects.forEach(project ->
            result.append(
                String.format("\n%d: due (%s), finished (%s)",
                        project.getId(), project.getDue(), project.getLastSprint().getName()))
        );

        return result.toString();
    }
}
