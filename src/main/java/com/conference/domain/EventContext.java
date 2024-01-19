package com.conference.domain;

import com.conference.domain.enumeration.EventContextStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventContext.
 */
@Entity
@Table(name = "event_context")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventContext implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_context_status")
    private EventContextStatus eventContextStatus;

    @Column(name = "start")
    private LocalDate start;

    @Column(name = "jhi_end")
    private LocalDate end;

    @OneToMany(mappedBy = "eventContext")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "eventContext" }, allowSetters = true)
    private Set<EventRegistration> eventContextRegistrations = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "eventContexts" }, allowSetters = true)
    private Event event;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventContext id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public EventContext name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventContextStatus getEventContextStatus() {
        return this.eventContextStatus;
    }

    public EventContext eventContextStatus(EventContextStatus eventContextStatus) {
        this.setEventContextStatus(eventContextStatus);
        return this;
    }

    public void setEventContextStatus(EventContextStatus eventContextStatus) {
        this.eventContextStatus = eventContextStatus;
    }

    public LocalDate getStart() {
        return this.start;
    }

    public EventContext start(LocalDate start) {
        this.setStart(start);
        return this;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return this.end;
    }

    public EventContext end(LocalDate end) {
        this.setEnd(end);
        return this;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Set<EventRegistration> getEventContextRegistrations() {
        return this.eventContextRegistrations;
    }

    public void setEventContextRegistrations(Set<EventRegistration> eventRegistrations) {
        if (this.eventContextRegistrations != null) {
            this.eventContextRegistrations.forEach(i -> i.setEventContext(null));
        }
        if (eventRegistrations != null) {
            eventRegistrations.forEach(i -> i.setEventContext(this));
        }
        this.eventContextRegistrations = eventRegistrations;
    }

    public EventContext eventContextRegistrations(Set<EventRegistration> eventRegistrations) {
        this.setEventContextRegistrations(eventRegistrations);
        return this;
    }

    public EventContext addEventContextRegistration(EventRegistration eventRegistration) {
        this.eventContextRegistrations.add(eventRegistration);
        eventRegistration.setEventContext(this);
        return this;
    }

    public EventContext removeEventContextRegistration(EventRegistration eventRegistration) {
        this.eventContextRegistrations.remove(eventRegistration);
        eventRegistration.setEventContext(null);
        return this;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public EventContext event(Event event) {
        this.setEvent(event);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventContext)) {
            return false;
        }
        return id != null && id.equals(((EventContext) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventContext{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", eventContextStatus='" + getEventContextStatus() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            "}";
    }
}
