package de.peteral.kapa.xstream;

import com.thoughtworks.xstream.XStream;
import de.peteral.kapa.domain.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;


public class Loader {
    public static Schedule load(String teamsUrl, String projectsUrl) {
        XStream xStream = new XStream( );
        xStream.processAnnotations(new Class[] {Projects.class, Teams.class, Task.class, Team.class});
        Teams teams = (Teams) xStream.fromXML(new File(teamsUrl));
        Projects projects = (Projects) xStream.fromXML(new File(projectsUrl));

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

        return new Schedule(teams.getTeams(), projects.getProjects());
    }
}
