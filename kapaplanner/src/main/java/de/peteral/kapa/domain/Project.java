package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import de.peteral.kapa.solver.ProjectLastSprintListener;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import java.util.List;

@XStreamAlias("Project")
@PlanningEntity
public class Project extends AbstractDomainObject {
    @XStreamImplicit
    private List<Task> tasks;

    @XStreamAsAttribute
    private int costsOfDelay;

    @XStreamAsAttribute
    private String due;

    @XStreamAsAttribute
    private String color;

    @CustomShadowVariable(variableListenerClass = ProjectLastSprintListener.class,
            sources = {@PlanningVariableReference(variableName = "lastSprint", entityClass = Task.class)})
    private Sprint lastSprint;

    public Project() {

    }

    public Project(long id, int costsOfDelay, String due, String color, List<Task> tasks) {
        super(id);
        this.tasks = tasks;
        this.costsOfDelay = costsOfDelay;
        this.due = due;
        this.color = color;
    }

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

    public Sprint getLastSprint() {
        return lastSprint;
    }

    public void setLastSprint(Sprint lastSprint) {
        this.lastSprint = lastSprint;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("Project-%d", getId());
    }
}
