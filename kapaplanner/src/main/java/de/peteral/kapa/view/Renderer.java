package de.peteral.kapa.view;

import de.peteral.kapa.domain.*;
import org.optaplanner.core.api.solver.Solver;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Renderer {
    private final List<String> sprintLabels;
    private final List<Team> teams;
    private final List<SprintEntry> sprints;
    private final Solver<Schedule> solver;
    private final Schedule schedule;

    private static final int SPRINT_HEIGHT = 100;
    private static final int SPRINT_WIDTH = 100;
    private static final int LATE_MARKER_HEIGHT = 10;
    private static final int BORDER_SIZE = 50;
    private static final int LEGEND_WIDTH = 200;
    private static final int LEGEND_HEIGHT = 80;

    public Renderer(Schedule schedule, Solver<Schedule> solver) {
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
        this.solver = solver;
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
        result = addLegend(result);
        result = addFooter(result);

        return result;
    }

    private String addFooter(String template) {
        return template.replaceAll("\\$\\{footer\\}",
                String.format("    <text x=\"50\" y=\"%d\" class=\"footer\"\n" +
                                "          onmouseover=\"document.getElementById('footer-tooltip').setAttribute('visibility', 'visible')\"\n" +
                                "          onmouseout=\"document.getElementById('footer-tooltip').setAttribute('visibility', 'hidden')\">Score: %s</text>\n" +
                                "\n" +
                                "    <g id=\"footer-tooltip\" visibility=\"hidden\" transform=\"translate(50, %d)\">\n" +
                                "        <rect class=\"tooltip\" width=\"300\" height=\"220\" rx=\"5\" ry=\"5\"/>\n" +
                                "        <text class=\"footer\">\n" +
                                "            <tspan x=\"10\" y=\"20\">[A/B/C/D/E/F/G]hard/[X/Y]soft</tspan>\n" +
                                "            <tspan x=\"20\" y=\"40\">A - Team must have required skill</tspan>\n" +
                                "            <tspan x=\"20\" y=\"60\">B - Sprint velocity</tspan>\n" +
                                "            <tspan x=\"20\" y=\"80\">C - Max task velocity per sprint</tspan>\n" +
                                "            <tspan x=\"20\" y=\"100\">D - One task one team</tspan>\n" +
                                "            <tspan x=\"20\" y=\"120\">E - External dependency</tspan>\n" +
                                "            <tspan x=\"20\" y=\"140\">F - Task dependency</tspan>\n" +
                                "            <tspan x=\"20\" y=\"160\">G - Due Task must be assigned</tspan>\n" +
                                "            <tspan x=\"20\" y=\"190\">X - Minimize costs of delay</tspan>\n" +
                                "            <tspan x=\"20\" y=\"210\">Y - High sprint utilization</tspan>\n" +
                                "        </text>\n" +
                                "    </g>",
                        getTotalHeight() - 20,
                        solver.getBestScore(),
                        getTotalHeight() - 260
                        ));
    }

    private String addLegend(String template) {
        final AtomicInteger i = new AtomicInteger();
        return template
                .replaceAll("\\$\\{legend\\}",
                        schedule.getProjects().stream()
                                .sorted(Comparator.comparing(project -> project.getLastSprint().getName()))
                        .map(project -> String.format("    <g transform=\"translate(%d, %d)\">\n" +
                                "        <rect width=\"%d\" height=\"%d\" rx=\"5\" ry=\"5\" class=\"tooltip\"/>\n" +
                                "        <text>\n" +
                                "            <tspan x=\"10\" y=\"20\" style=\"font-weight:bold\">%s</tspan>\n" +
                                "            <tspan x=\"20\" y=\"40\">Due: %s</tspan>\n" +
                                "            <tspan x=\"20\" y=\"60\" %s>Finished: %s</tspan>\n" +
                                "        </text>\n" +
                                "        <rect width=\"20\" height=\"60\" y=\"10\" x=\"170\" class=\"%s\"/>\n" +
                                "    </g>\n",
                                (int)(1.5 * BORDER_SIZE + teams.size() * SPRINT_WIDTH),
                                (int)(0.5 * BORDER_SIZE + LEGEND_HEIGHT * i.getAndIncrement()),
                                LEGEND_WIDTH,
                                LEGEND_HEIGHT - 4,
                                project,
                                project.getDue(),
                                (project.getDue() != null && project.getDue().compareTo(project.getLastSprint().getName()) < 0) ? " class=\"late\"" : "",
                                project.getLastSprint().getName(),
                                project
                        ))
                        .collect(Collectors.joining("\n"))
                );
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
                                        int width = (int)(100.0 * totalWork / sprint.velocity);
                                        int x = BORDER_SIZE + 1 + sprint.teamIndex * SPRINT_WIDTH + sprint.getCurrentOffset();
                                        int y = BORDER_SIZE + 1 + sprint.sprintIndex * SPRINT_HEIGHT;

                                        result.append(String.format("        <rect height=\"%d\" width=\"%d\" x=\"%d\" y=\"%d\"/>\n",
                                                SPRINT_HEIGHT - 2, width, x, y));
                                        sprint.addTask(width);

                                        if (project.getDue() != null && project.getDue().compareTo(project.getLastSprint().getName()) < 0)
                                            result.append(String.format("        <rect class=\"late\" height=\"%d\" width=\"%d\" x=\"%d\" y=\"%d\"/>\n",
                                                    LATE_MARKER_HEIGHT, width, x, y + SPRINT_HEIGHT - LATE_MARKER_HEIGHT));

                                        long violations = sprint.tasks.stream().map(subTask -> subTask.getTask())
                                                .filter(task -> task.getProject() == project)
                                                .collect(Collectors.summarizingLong(Task::dependenciesViolated)).getSum();

                                        if (violations > 0)
                                            result.append(String.format("        <rect class=\"late\" height=\"%d\" width=\"%d\" x=\"%d\" y=\"%d\"/>\n",
                                                    LATE_MARKER_HEIGHT, width, x, y + SPRINT_HEIGHT - 2*LATE_MARKER_HEIGHT));

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
                                                "            <rect width=\"%d\" height=\"%d\"/>\n" +
                                                "            <text class=\"sprint-utilization\" x=\"5\" y=\"15\">%d / %d</text>\n" +
                                                "        </g>",
                                        BORDER_SIZE + sprint.teamIndex * SPRINT_WIDTH,
                                        BORDER_SIZE + sprint.sprintIndex * SPRINT_HEIGHT,
                                        SPRINT_WIDTH,
                                        SPRINT_HEIGHT,
                                        sprint.utilization,
                                        sprint.velocity))
                                .collect(Collectors.joining("\n")));
    }

    private String addSprintLabels(String template) {
        final AtomicInteger i = new AtomicInteger();
        return template.replaceAll("\\$\\{sprint-labels\\}",
                sprintLabels.stream()
                        .map(s -> String.format("        <text x=\"10\" y=\"%d\">%s</text>",
                                BORDER_SIZE + SPRINT_HEIGHT / 2 + 10 + (i.getAndIncrement()) * SPRINT_HEIGHT,
                                s))
                        .collect(Collectors.joining("\n")));

    }

    private String addSize(String template) {
        return template
            .replaceAll("\\$\\{width\\}", Integer.toString(teams.size() * SPRINT_WIDTH + 2 * BORDER_SIZE + LEGEND_WIDTH))
            .replaceAll("\\$\\{height\\}", Integer.toString(getTotalHeight()));
    }

    private int getTotalHeight() {
        return Math.max(
                sprintLabels.size() * SPRINT_HEIGHT + 2 * BORDER_SIZE,
                LEGEND_HEIGHT * schedule.getProjects().size() + BORDER_SIZE);
    }

    private String addTeamLabels(String template) {
        final AtomicInteger i = new AtomicInteger();
        return template.replaceAll("\\$\\{team-labels\\}",
                teams.stream()
                        .map(team -> String.format("        <text x=\"%d\" y=\"30\">%s</text>",
                                BORDER_SIZE + 5 + (i.getAndIncrement()) * SPRINT_WIDTH, team.getName()))
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
