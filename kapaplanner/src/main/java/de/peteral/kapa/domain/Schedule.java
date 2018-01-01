package de.peteral.kapa.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.bendable.BendableScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.*;

@PlanningSolution
public class Schedule {
    @ValueRangeProvider(id = "sprintRange")
    @ProblemFactCollectionProperty
    private List<Sprint> sprints;

    @PlanningEntityCollectionProperty
    private List<SubTask> subTasks;

    @PlanningEntityCollectionProperty
    private List<Task> tasks;

    @PlanningScore(bendableHardLevelsSize = 7, bendableSoftLevelsSize = 1)
    private BendableScore score;

    private List<Team> teams;

    @PlanningEntityCollectionProperty
    private List<Project> projects;

    public Schedule(List<Team> teams, List<Project> projects) {
        this.teams = teams;
        this.projects = projects;
        this.sprints = new ArrayList<>();
        this.subTasks = new ArrayList<>();
        this.tasks = new ArrayList<>();
        projects.forEach(project -> project.getTasks().forEach(task ->  {
            this.tasks.add(task);
            task.getSubtasks().forEach(this.subTasks::add);
        }));
        teams.forEach(team -> team.getSprints().forEach(this.sprints::add));
    }

    public Schedule() {

    }

    public BendableScore getScore() {
        return score;
    }

    public void setScore(BendableScore score) {
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
