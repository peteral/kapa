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

    @XStreamAsAttribute
    private String name;

    public Team() {

    }

    public List<String> getSkills() {
        return skills;
    }

    @Override
    public String toString() {
        return String.format("Team-%1d", getId());
    }

    @Override
    public String getLabel() {

        return new StringBuilder("\n")
                .append(String.format("Team-%1d (%2s): ", getId(), getName()))
                .toString();
    }

    public List<Sprint> getSprints() {
        return sprints;
    }

    public void setSprints(List<Sprint> sprints) {
        this.sprints = sprints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
