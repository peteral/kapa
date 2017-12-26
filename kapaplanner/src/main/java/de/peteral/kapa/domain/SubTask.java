package de.peteral.kapa.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class SubTask extends AbstractDomainObject {
    private long work;

    private static long sequence = 1;

    @PlanningVariable(valueRangeProviderRefs = {"sprintRange"})
    private Sprint sprint;

    private Task task;

    public SubTask(Task task, long work) {
        super(sequence++);
        this.task = task;
        this.work = work;
    }

    public SubTask() {

    }


    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public long getWork() {
        return work;
    }

    public void setWork(long work) {
        this.work = work;
    }

    @Override
    public String toString() {
        return String.format("Subtask-%d", getId());
    }

    @Override
    public String getLabel() {
        return String.format("Subtask-%d (%s - %d)", getId(), getTask().getId(), getWork());
    }


    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
