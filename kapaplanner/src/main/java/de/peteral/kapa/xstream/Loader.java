package de.peteral.kapa.xstream;

import com.thoughtworks.xstream.XStream;
import de.peteral.kapa.domain.*;

import java.net.URL;


public class Loader {
    public static Schedule load(URL teamsUrl, URL tasksUrl) {
        XStream xStream = new XStream( );
        xStream.processAnnotations(new Class[] {Tasks.class, Teams.class, Task.class, Team.class});
        Teams teams = (Teams) xStream.fromXML(teamsUrl);
        Tasks tasks = (Tasks) xStream.fromXML(tasksUrl);
        return new Schedule(teams.getTeams(), tasks.getTasks());
    }
}
