package de.peteral.kapa.xstream;

import com.thoughtworks.xstream.XStream;
import de.peteral.kapa.domain.*;

import java.net.URL;


public class Loader {
    public static Schedule load(URL teamsUrl, URL projectsUrl) {
        XStream xStream = new XStream( );
        xStream.processAnnotations(new Class[] {Projects.class, Teams.class, Task.class, Team.class});
        Teams teams = (Teams) xStream.fromXML(teamsUrl);
        Projects projects = (Projects) xStream.fromXML(projectsUrl);
        // set task -> parent project references
        projects.getProjects().stream().forEach(
                project -> project.getTasks().stream().forEach(
                        task -> task.setProject(project)));

        return new Schedule(teams.getTeams(), projects.getProjects());
    }
}
