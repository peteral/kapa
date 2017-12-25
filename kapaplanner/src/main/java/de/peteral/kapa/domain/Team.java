package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@XStreamAlias("Team")
public class Team extends AbstractDomainObject {

    @XStreamImplicit(itemFieldName = "Skill")
    private List<String> skills;

    @XStreamImplicit(itemFieldName = "Sprint")
    private List<Sprint> sprints;

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

        return new StringBuilder("\n")
                .append(String.format("Team-%1d (%2s): ", getId(), getSkills()))
                .append(getSprints().stream().map(sprint -> sprint.getFullString()).collect(Collectors.joining(", ")))
                .toString();
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

}
