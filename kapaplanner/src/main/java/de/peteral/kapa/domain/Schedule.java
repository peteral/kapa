package de.peteral.kapa.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PlanningSolution
public class Schedule {
    @ValueRangeProvider(id = "sprintRange")
    @ProblemFactCollectionProperty
    private List<Sprint> sprints;

    @PlanningEntityCollectionProperty
    private List<SubTask> subTasks;

    @PlanningScore
    private HardSoftScore score;

    private List<Team> teams;

    public Schedule(List<Team> teams, List<Project> projects) {
        this.teams = teams;
        this.sprints = new ArrayList<>();
        this.subTasks = new ArrayList<>();
        projects.forEach(project -> project.getTasks().forEach(task -> task.getSubtasks().forEach(this.subTasks::add)));
        teams.forEach(team -> team.getSprints().forEach(this.sprints::add));
    }

    public Schedule() {

    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return teams.toString();
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }
}
