package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("Project")
public class Project extends AbstractDomainObject {
    @XStreamImplicit
    private List<Task> tasks;

    @XStreamAsAttribute
    private int costsOfDelay;

    @XStreamAsAttribute
    private String due;

    // TODO implement probability of project intake

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getCostsOfDelay() {
        return costsOfDelay;
    }

    public void setCostsOfDelay(int costsOfDelay) {
        this.costsOfDelay = costsOfDelay;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }
}
