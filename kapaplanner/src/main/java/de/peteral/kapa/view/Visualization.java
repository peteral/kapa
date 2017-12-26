package de.peteral.kapa.view;

import de.peteral.kapa.domain.*;

import java.util.*;
import java.util.stream.Collectors;

public class Visualization {
    private final Map<Team, Map<Sprint, Map<Task, List<SubTask>>>> data;
    private final Map<Project, Sprint> projects;

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

        projects = new HashMap<>();
        schedule.getTasks().forEach(task -> {
            Sprint lastProjectSprint = projects.get(task.getProject());
            if (lastProjectSprint == null || lastProjectSprint.getName().compareTo(task.getLastSprint().getName()) < 0)
                projects.put(task.getProject(), task.getLastSprint());
        });
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
                tasks.forEach((task, subTasks) ->
                    result.append("\n\t\t").append(task.getLabel()).append("/").append(
                            subTasks.stream().collect(Collectors.summarizingLong(SubTask::getWork)).getSum()
                    )
                );
            });
        });

        result.append("\nProjects:\n")
            .append("-----------------------------------");

        projects.forEach(((project, sprint) ->
            result.append(
                String.format("\n%d: due (%s), finished (%s)", project.getId(), project.getDue(), sprint.getName()))
        ));

        return result.toString();
    }
}
