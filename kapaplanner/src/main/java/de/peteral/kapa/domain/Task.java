package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@XStreamAlias("Task")
public class Task extends AbstractDomainObject {
    @XStreamAsAttribute
    private String skill;
    @XStreamAsAttribute
    private int work;

    private List<SubTask> subtasks;

    @XStreamAlias("PreviousTask")
    private Task previousTask;

    private Project project;


    public Task(long id, String skill, int work) {
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

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    @Override
    public String toString() {
        return String.format("Task-%d (%s - %d)", getId(), getSkill(), getWork());
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
        IntStream.range(0, getWork()).forEach(i -> this.subtasks.add(new SubTask(this, 1)));
    }
}
