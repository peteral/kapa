package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@XStreamAlias("Sprint")
public class Sprint extends AbstractDomainObject {

    @XStreamAsAttribute
    private long velocity;

    @XStreamAsAttribute
    private String name;

    private Team team;

    public Sprint() {

    }

    @Override
    public String toString() {
        return String.format("Sprint-%d", getId());
    }

    @Override
    public String getLabel() {
        return new StringBuilder()
                .append(String.format("Sprint %1s (%2d) %3d/", getName(), getId(), getVelocity()))
                .toString();
    }

    public long getVelocity() {
        return velocity;
    }

    public void setVelocity(long velocity) {
        this.velocity = velocity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

}
