package de.peteral.kapa.view;

import de.peteral.kapa.domain.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Renderer {
    private final List<String> sprintLabels;
    private final List<Team> teams;
    private final List<SprintEntry> sprints;
    private final Schedule schedule;

    private static final String LATE_PROJECT_COLOR = "#da0d0d";

    public Renderer(Schedule schedule) {
        this.schedule = schedule;

        sprintLabels = schedule.getSprints().stream()
                .map(sprint -> sprint.getName())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        teams = schedule.getSprints().stream()
                .map(sprint -> sprint.getTeam())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        sprints = schedule.getSprints().stream()
                .map(sprint -> new SprintEntry(
                        teams.indexOf(sprint.getTeam()),
                        sprintLabels.indexOf(sprint.getName()),
                        sprint.getVelocity(),
                        schedule.getSubTasks().stream()
                            .filter(subTask -> subTask.getSprint() == sprint)
                            .collect(Collectors.summarizingLong(SubTask::getWork))
                            .getSum(),
                        schedule.getSubTasks().stream()
                                .filter(subTask -> subTask.getSprint() == sprint)
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public String render() throws URISyntaxException, IOException {
        StringBuilder templateBuilder = new StringBuilder();
        try ( Stream<String> lines = Files.lines(Paths.get(getClass().getResource("template.svg").toURI()))) {
            lines.forEach(line -> templateBuilder.append(line).append("\n"));
        }

        String result = templateBuilder.toString();
        result = addSize(result);
        result = addSprintLabels(result);
        result = addTeamLabels(result);
        result = addProjectStyles(result);
        result = addProjects(result);
        result = addSprints(result);

        return result;
    }

    private String addProjectStyles(String template) {
        return template
                .replaceAll("\\$\\{project-styles\\}",
                        schedule.getProjects().stream()
                            .map(project -> String.format("                .%s {\n" +
                                                "                    fill:           %s;\n" +
                                                "                    stroke-width:   0;\n" +
                                                "                }",
                                        project.toString(),
                                        project.getColor()))
                            .collect(Collectors.joining("\n"))
                );
    }

    private String addProjects(String template) {
        return template
                .replaceAll("\\$\\{projects\\}",
                        schedule.getProjects().stream()
                            .map(project -> {
                                final StringBuilder result = new StringBuilder("    <g class=\"")
                                        .append(project.toString())
                                        .append(("\">\n"));

                                sprints.stream().forEach(sprint -> {
                                    long totalWork = sprint.tasks.stream()
                                            .filter(task -> task.getTask().getProject() == project)
                                            .collect(Collectors.summarizingLong(SubTask::getWork)).getSum();

                                    if (totalWork > 0) {
                                        int width = (int)(100.0 * totalWork / sprint.velocity) - 2;
                                        int x = 51 + sprint.teamIndex * 100 + sprint.getCurrentOffset();
                                        int y = 51 + sprint.sprintIndex * 100;

                                        result.append(String.format("        <rect height=\"98\" width=\"%d\" x=\"%d\" y=\"%d\"/>\n",
                                                width, x, y));
                                        sprint.addTask(width);

                                        if (project.getDue() != null && project.getDue().compareTo(project.getLastSprint().getName()) < 0)
                                            result.append(String.format("        <rect class=\"late\" height=\"10\" width=\"%d\" x=\"%d\" y=\"%d\"/>\n",
                                                    width, x, y + 90));

                                    }
                                });

                                result.append("    </g>");
                                return result.toString();
                            })
                            .collect(Collectors.joining("\n"))
                );
    }

    private String addSprints(String template) {
        return template
                .replaceAll("\\$\\{sprints\\}",
                        sprints.stream()
                                .map(sprint -> String.format("        <g transform=\"translate(%d, %d)\">\n" +
                                                "            <rect width=\"100\" height=\"100\"/>\n" +
                                                "            <text class=\"sprint-utilization\" x=\"5\" y=\"15\">%d / %d</text>\n" +
                                                "        </g>",
                                        50 + sprint.teamIndex * 100,
                                        50 + sprint.sprintIndex * 100,
                                        sprint.utilization,
                                        sprint.velocity))
                                .collect(Collectors.joining("\n")));
    }

    private String addSprintLabels(String template) {
        final AtomicInteger i = new AtomicInteger();
        return template.replaceAll("\\$\\{sprint-labels\\}",
                sprintLabels.stream()
                        .map(s -> String.format("        <text x=\"10\" y=\"%d\">%s</text>",
                                110 + (i.getAndIncrement()) * 100,
                                s))
                        .collect(Collectors.joining("\n")));

    }

    private String addSize(String template) {
        return template
            .replaceAll("\\$\\{width\\}", Integer.toString(teams.size() * 100 + 100))
            .replaceAll("\\$\\{height\\}", Integer.toString(sprintLabels.size() * 100 + 100));
    }

    private String addTeamLabels(String template) {
        final AtomicInteger i = new AtomicInteger();
        return template.replaceAll("\\$\\{team-labels\\}",
                teams.stream()
                        .map(team -> String.format("        <text x=\"%d\" y=\"30\">%s</text>",
                                55 + (i.getAndIncrement()) * 100, team.toString()))
                        .collect(Collectors.joining("\n")));
    }

    private class SprintEntry {
        int teamIndex;
        int sprintIndex;
        int currentOffset;
        long velocity;
        long utilization;
        private List<SubTask> tasks;

        public SprintEntry(int teamIndex, int sprintIndex, long velocity, long utilization, List<SubTask> tasks) {
            this.teamIndex = teamIndex;
            this.sprintIndex = sprintIndex;
            this.velocity = velocity;
            this.utilization = utilization;
            this.tasks = tasks;
            currentOffset = 0;
        }

        public int getCurrentOffset() {
            return currentOffset;
        }

        public void addTask(int width) {
            this.currentOffset += width;
        }
    }
}
