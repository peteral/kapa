package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.peteral.kapa.solver.TaskDifficultyComparator;
import de.peteral.kapa.solver.TeamStrengthComparator;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity(difficultyComparatorClass = TaskDifficultyComparator.class)
@XStreamAlias("Task")
public class Task extends AbstractDomainObject {
    @XStreamAsAttribute
    private Capability capability;
    @XStreamAsAttribute
    private int work;

    @PlanningVariable(valueRangeProviderRefs = {"teamRange"}, strengthComparatorClass = TeamStrengthComparator.class)
    private Team team;

    public Task(long id, Capability capability, int work) {
        super(id);
        this.capability = capability;
        this.work = work;
    }

    public Task() {

    }

    public Capability getCapability() {
        return capability;
    }

    public void setCapability(Capability capability) {
        this.capability = capability;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    @Override
    public String toString() {
        return String.format("Task-%1d", getId());
    }
}
