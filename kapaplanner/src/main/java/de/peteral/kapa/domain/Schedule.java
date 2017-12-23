package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import de.peteral.kapa.xstream.XStreamFactory;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
@XStreamAlias("Schedule")
public class Schedule {
    @ValueRangeProvider(id = "taskRange")
    @PlanningEntityCollectionProperty
    @XStreamImplicit
    private List<Task> tasks;

    @ValueRangeProvider(id = "teamRange")
    @ProblemFactCollectionProperty
    @XStreamImplicit
    private List<Team> teams;

    @PlanningScore
    private HardSoftScore score;

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return XStreamFactory.create().toXML(this);
    }
}
