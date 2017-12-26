package de.peteral.kapa.domain;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDomainObject that = (AbstractDomainObject) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public String getLabel() {
        return toString();
    }
}
