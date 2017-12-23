package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Arrays;
import java.util.List;

@XStreamAlias("Team")
public class Team extends AbstractDomainObject {

    @XStreamImplicit(itemFieldName = "Capability")
    private List<Capability> capabilities;

    public Team(long id, Capability... capabilities) {
        super(id);
        this.capabilities = Arrays.asList(capabilities);
    }

    public Team() {

    }

    public List<Capability> getCapabilities() {
        return capabilities;
    }

    @Override
    public String toString() {
        return String.format("Team-%1d", getId());
    }
}
