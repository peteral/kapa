package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import de.peteral.kapa.solver.TaskDifficultyComparator;
import de.peteral.kapa.solver.TeamStrengthComparator;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@PlanningEntity(difficultyComparatorClass = TaskDifficultyComparator.class)
@XStreamAlias("Task")
public class Task extends AbstractDomainObject {
    @XStreamAsAttribute
    private Skill skill;
    @XStreamAsAttribute
    private int work;

    @PlanningVariable(valueRangeProviderRefs = {"teamRange"}, strengthComparatorClass = TeamStrengthComparator.class)
    private Team team;

    @XStreamAlias("PreviousTask")
    private Task previousTask;

    @PlanningVariable(valueRangeProviderRefs = {"startTimeRange"})
    private Integer startTime;


    public Task(long id, Skill skill, int work) {
        super(id);
        this.skill = skill;
        this.work = work;
    }

    public Task() {

    }

    @ValueRangeProvider(id = "startTimeRange")
    public List<Integer> getPossibleTimes() {
        return IntStream.rangeClosed(0, 500).boxed().collect(Collectors.toList());
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
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
        return String.format("Task-%d (%s - %d)", getId(), getSkill(), getWork());
    }

    public Task getPreviousTask() {
        return previousTask;
    }

    public void setPreviousTask(Task previousTask) {
        this.previousTask = previousTask;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
