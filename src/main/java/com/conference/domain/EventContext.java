package com.conference.domain;

import com.conference.domain.enumeration.EventContextStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "event_context_status", nullable = false)
    private EventContextStatus eventContextStatus;

    @NotNull
    @Column(name = "start", nullable = false)
    private Instant start;

    @NotNull
    @Column(name = "jhi_end", nullable = false)
    private Instant end;

    @JsonIgnoreProperties(value = { "eventContext" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Room eventContextRoom;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "eventContext")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "eventCounterparty", "eventContext" }, allowSetters = true)
    private Set<EventRegistration> eventContextRegistrations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "appUser" }, allowSetters = true)
    private ApplicationUser contextHost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "eventContexts", "mainHost" }, allowSetters = true)
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

    public String getDescription() {
        return this.description;
    }

    public EventContext description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Instant getStart() {
        return this.start;
    }

    public EventContext start(Instant start) {
        this.setStart(start);
        return this;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return this.end;
    }

    public EventContext end(Instant end) {
        this.setEnd(end);
        return this;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public Room getEventContextRoom() {
        return this.eventContextRoom;
    }

    public void setEventContextRoom(Room room) {
        this.eventContextRoom = room;
    }

    public EventContext eventContextRoom(Room room) {
        this.setEventContextRoom(room);
        return this;
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

    public ApplicationUser getContextHost() {
        return this.contextHost;
    }

    public void setContextHost(ApplicationUser applicationUser) {
        this.contextHost = applicationUser;
    }

    public EventContext contextHost(ApplicationUser applicationUser) {
        this.setContextHost(applicationUser);
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
        return getId() != null && getId().equals(((EventContext) o).getId());
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
            ", description='" + getDescription() + "'" +
            ", eventContextStatus='" + getEventContextStatus() + "'" +
            ", start='" + getStart() + "'" +
            ", end='" + getEnd() + "'" +
            "}";
    }
}
