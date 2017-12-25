package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@XStreamAlias("Team")
@PlanningEntity
public class Team extends AbstractDomainObject {

    @XStreamImplicit(itemFieldName = "Skill")
    private List<String> skills;

    @InverseRelationShadowVariable(sourceVariableName = "team")
    @XStreamAlias("Projects")
    private List<Task> tasks;

    @XStreamAsAttribute
    private int velocity;

    public Team(long id, String... capabilities) {
        super(id);
        this.skills = Arrays.asList(capabilities);
    }

    public Team() {

    }

    public List<String> getSkills() {
        return skills;
    }

    @Override
    public String toString() {

        return new StringBuilder(String.format("Team-%1d (%2s - %d): ", getId(), getSkills(), getTotalWork()))
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

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}
