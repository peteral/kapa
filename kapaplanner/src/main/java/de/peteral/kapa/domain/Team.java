package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@XStreamAlias("Team")
@PlanningEntity
public class Team extends AbstractDomainObject {

    @XStreamImplicit(itemFieldName = "Capability")
    private List<Capability> capabilities;

    @InverseRelationShadowVariable(sourceVariableName = "team")
    @XStreamAlias("Tasks")
    private List<Task> tasks;

    public Team(long id, Capability... capabilities) {
        super(id);
        this.capabilities = Arrays.asList(capabilities);
    }

    public Team() {

    }

    public List<Capability> getCapabilities() {
        return capabilities;
    }

    @Override
    public String toString() {

        return new StringBuilder(String.format("Team-%1d (%2s - %d): ", getId(), getCapabilities(), getTotalWork()))
                .append(getTasks().stream().map(task -> task.toString()).collect(Collectors.joining(", ")))
                .toString();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getTotalWork() {
        return getTasks().stream().collect(Collectors.summingInt(Task::getWork));
    }
}
