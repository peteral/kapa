package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@XStreamAlias("Sprint")
@PlanningEntity
public class Sprint extends AbstractDomainObject {

    @XStreamAsAttribute
    private int velocity;

    @XStreamAsAttribute
    private String name;

    // TODO this shadow variable is currently not being updated
    @InverseRelationShadowVariable(sourceVariableName = "sprint")
    private List<SubTask> subTasks;

    private Team team;

    public Sprint() {

    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(String.format("Sprint-%1d (%2s - %3s)", getId(), getTeam().getId(), getName()))
                .toString();
    }

    public String getFullString() {
        List<Task> tasks = subTasks.stream().map(subtask -> subtask.getTask()).distinct().collect(Collectors.toList());
        return new StringBuilder("\n")
                .append(String.format("Sprint-%1d (%2s - %3d): ", getId(), getName(), getVelocity()))
                .append(tasks)
                .toString();
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }
}
