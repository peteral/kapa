package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
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
    @ValueRangeProvider(id = "taskRange")
    @PlanningEntityCollectionProperty
    private List<Task> tasks;

    @ValueRangeProvider(id = "teamRange")
    @ProblemFactCollectionProperty
    private List<Team> teams;

    private List<Project> projects;

    @PlanningScore
    private HardSoftScore score;

    public Schedule(List<Team> teams, List<Project> projects) {

        this.teams = teams;
        this.projects = projects;
        this.tasks = new ArrayList<>();
        projects.stream().forEach(project -> project.getTasks().stream().forEach(this.tasks::add));
    }

    public Schedule() {

    }

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
        return new StringBuilder()
                .append("\n\tUnassigned tasks:\t")
                .append(tasks.stream().filter(task -> task.getTeam() == null).map(task -> "" + task).collect(Collectors.joining(", ")))
                .append("\n\tAssigned tasks:\n\t\t")
                .append(teams.stream().map(team -> team.toString()).collect(Collectors.joining("\n\t\t")).toString()).toString();
    }
}
