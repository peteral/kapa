package de.peteral.kapa.xstream;

import com.thoughtworks.xstream.XStream;
import de.peteral.kapa.domain.*;
import javafx.util.Pair;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


public class Loader {
    public static Schedule load(String teamsUrl, String projectsUrl) {
        XStream xStream = new XStream( );
        xStream.processAnnotations(new Class[] {Projects.class, Teams.class, Task.class, Team.class});
        Teams teams = (Teams) xStream.fromXML(new File(teamsUrl));
        Projects projects = (Projects) xStream.fromXML(new File(projectsUrl));
        return createSchedule(teams, projects);
    }

    public static Schedule createSchedule(Teams teams, Projects projects) {
        // set task -> parent project references + generate sub tasks
        projects.getProjects().forEach(
                project -> project.getTasks().forEach(
                        task -> {
                            task.setProject(project);
                            task.generateSubTasks();
                        }));

        // set sprint -> parent team reference, initialize subtask list
        teams.getTeams().forEach(
                team -> team.getSprints().forEach(
                        sprint -> {
                            sprint.setTeam(team);
                        })
        );

        // initialize missing color attributes to procedurally generated color
        projects.getProjects().stream()
                .filter(project -> project.getColor() == null)
                .forEach(project -> project.setColor(getColor(project.getId())));

        return new Schedule(teams.getTeams(), projects.getProjects());
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


}
