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
    private int velocity;

    @XStreamAsAttribute
    private String name;

    private Team team;

    public Sprint() {

    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(String.format("Sprint-%1d (%2s - %3s = %4d)", getId(), getTeam().getId(), getName(), getVelocity()))
                .toString();
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
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
