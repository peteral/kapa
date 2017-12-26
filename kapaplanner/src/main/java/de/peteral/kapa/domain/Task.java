package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.peteral.kapa.solver.FirstSprintListener;
import de.peteral.kapa.solver.LastSprintListener;
import de.peteral.kapa.solver.TaskMaxVelocityListener;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@XStreamAlias("Task")
@PlanningEntity
public class Task extends AbstractDomainObject {
    @XStreamAsAttribute
    private String skill;
    @XStreamAsAttribute
    private long work;

    private List<SubTask> subtasks;

    @XStreamAlias("PreviousTask")
    @ProblemFactProperty
    private Task previousTask;

    private Project project;

    @XStreamAsAttribute
    private int maxVelocity;

    @CustomShadowVariable(
            variableListenerClass = TaskMaxVelocityListener.class,
            sources = {@PlanningVariableReference(variableName = "sprint", entityClass = SubTask.class)})
    private Boolean sprintViolatesMaxVelocity;

    @CustomShadowVariable(
            variableListenerClass = FirstSprintListener.class,
            sources = {@PlanningVariableReference(variableName = "sprint", entityClass = SubTask.class)})
    private Sprint firstSprint;

    @CustomShadowVariable(
            variableListenerClass = LastSprintListener.class,
            sources = {@PlanningVariableReference(variableName = "sprint", entityClass = SubTask.class)})
    private Sprint lastSprint;

    public Task(long id, String skill, long work) {
        super(id);
        this.skill = skill;
        this.work = work;
    }

    public Task() {

    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public long getWork() {
        return work;
    }

    public void setWork(long work) {
        this.work = work;
    }

    @Override
    public String toString() {
        return String.format("Task-%d (%s) (B: %s) - %d",
                getId(), getSkill(), (getPreviousTask() == null) ? "n.A." : getPreviousTask().getId(), getWork());
    }

    public Task getPreviousTask() {
        return previousTask;
    }

    public void setPreviousTask(Task previousTask) {
        this.previousTask = previousTask;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<SubTask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<SubTask> subtasks) {
        this.subtasks = subtasks;
    }

    public void generateSubTasks() {
        this.subtasks = new ArrayList<>();
        LongStream.range(0, getWork()).forEach(i -> this.subtasks.add(new SubTask(this, 1)));
    }

    public int getMaxVelocity() {
        return maxVelocity;
    }

    public void setMaxVelocity(int maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public Boolean getSprintViolatesMaxVelocity() {
        return (sprintViolatesMaxVelocity == null) ? Boolean.FALSE : sprintViolatesMaxVelocity;
    }

    public void setSprintViolatesMaxVelocity(Boolean sprintViolatesMaxVelocity) {
        this.sprintViolatesMaxVelocity = sprintViolatesMaxVelocity;
    }

    public Sprint getLastSprint() {
        return lastSprint;
    }

    public void setLastSprint(Sprint lastSprint) {
        this.lastSprint = lastSprint;
    }

    public Sprint getFirstSprint() {
        return firstSprint;
    }

    public void setFirstSprint(Sprint firstSprint) {
        this.firstSprint = firstSprint;
    }
}
