package de.peteral.kapa.xstream;

import com.thoughtworks.xstream.XStream;
import de.peteral.kapa.domain.Schedule;
import de.peteral.kapa.domain.Task;
import de.peteral.kapa.domain.Team;

public class XStreamFactory {
    public static XStream create() {
        XStream xStream = new XStream( );
        xStream.processAnnotations(new Class[] {Schedule.class, Task.class, Team.class});
        return xStream;
    }
}
