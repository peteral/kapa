package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.io.Serializable;

public class AbstractDomainObject implements Serializable, Comparable<AbstractDomainObject> {
    @PlanningId
    @XStreamAsAttribute
    private Long id;

    public AbstractDomainObject(long id) {
        this.id = id;
    }

    public AbstractDomainObject() {

    }

    public int compareTo(AbstractDomainObject o) {
        return id.compareTo(o.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
