package de.peteral.kapa.xstream;

import com.thoughtworks.xstream.XStream;
import de.peteral.kapa.domain.*;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class Simulation {
    private static final List<String> SPRINT_NAMES = IntStream.rangeClosed(20, 60).boxed()
            .map(i -> String.format("7.%d", i))
            .collect(Collectors.toList());
    private static final AtomicLong sprint_sequence = new AtomicLong();
    private static final AtomicLong task_sequence = new AtomicLong();

    // simulates a realistic scenario:
    // 16 sprints
    // 6 teams
    // 50 projects 100 SP each
    public static Schedule simulate() {
        final Teams teams = new Teams();
        teams.setTeams(Arrays.asList(
                new Team(1, "RE", Arrays.asList("ANALYSIS"), createSprints(50)),
                new Team(2, "DEV-1", Arrays.asList("PLATFORM"), createSprints(55)),
                new Team(3, "DEV-2", Arrays.asList("PLATFORM"), createSprints(45)),
                new Team(4, "EXT-1", Arrays.asList("PROJECT"), createSprints(56)),
                new Team(5, "EXT-2", Arrays.asList("PROJECT"), createSprints(61)),
                new Team(6, "OPS", Arrays.asList("COMMISSIONING"), createSprints(50))
        ));

        final Projects projects = new Projects();
        projects.setProjects(LongStream.rangeClosed(1, 50).boxed()
                .map(id -> new Project(id, 1, null, getColor(id), createTasks(id)))
                .collect(Collectors.toList()));


        // seed some constraints
        IntStream.of(10, 20, 30, 12, 22, 32).forEach(i -> {
            Project project = projects.getProjects().get(i);
            project.setDue("7.30");
            project.setCostsOfDelay(1);
        });
        IntStream.of(11, 21, 31, 13, 23, 33).forEach(i -> {
            Project project = projects.getProjects().get(i);
            project.setDue("7.40");
            project.setCostsOfDelay(1);
        });
        // TODO dependencies between projects
        Task blocker = projects.getProjects().get(10).getTasks().stream()
                .filter(task -> task.getSkill().equalsIgnoreCase("analysis"))
                .collect(Collectors.toList())
                .get(0)
                ;
        setBlocker(projects, IntStream.of(12, 22, 32), blocker, "PLATFORM");

        blocker = projects.getProjects().get(11).getTasks().stream()
                .filter(task -> task.getSkill().equalsIgnoreCase("analysis"))
                .collect(Collectors.toList())
                .get(0)
                ;
        setBlocker(projects, IntStream.of(13, 23, 33), blocker, "PLATFORM");

        return Loader.createSchedule(teams, projects);
    }

    private static void setBlocker(Projects projects, IntStream projectIndexes, Task blocker, String skill) {
        projectIndexes.forEach(i -> {
            Project project = projects.getProjects().get(i);
            project.getTasks().stream()
                    .filter(task -> task.getSkill().equalsIgnoreCase(skill))
                    .forEach(task -> task.setPreviousTasks(Arrays.asList(blocker)));
        });
    }

    private static List<Task> createTasks(Long id) {
        Task analysis = new Task(task_sequence.incrementAndGet(), "ANALYSIS", 10);
        Task product1 = new Task(task_sequence.incrementAndGet(), "PLATFORM", 20);
        Task product2 = new Task(task_sequence.incrementAndGet(), "PLATFORM", 20);
        Task project = new Task(task_sequence.incrementAndGet(), "PROJECT", 40);
        Task customizing = new Task(task_sequence.incrementAndGet(), "COMMISSIONING", 10);

        analysis.setMaxVelocity(5);
        product1.setPreviousTasks(Arrays.asList(analysis));
        product2.setPreviousTasks(Arrays.asList(analysis));
        project.setPreviousTasks(Arrays.asList(product1, product2));
        customizing.setPreviousTasks(Arrays.asList(project));

        // foked up the order on purpose to check difficulty comparator
        return Arrays.asList(customizing, project, analysis, product1, product2);
    }

    private final static Color MIX = Color.LIGHT_GRAY;

    private static String getColor(Long id) {
        Random random = new Random(id);
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        red = (red + MIX.getRed()) / 2;
        green = (green + MIX.getGreen()) / 2;
        blue = (blue + MIX.getBlue()) / 2;

        return String.format("#%x%x%x", red, green, blue);
    }

    private static List<Sprint> createSprints(int velocity) {
        return SPRINT_NAMES.stream()
                .map(name -> new Sprint(sprint_sequence.incrementAndGet(), velocity, name))
                .collect(Collectors.toList());
    }
}
