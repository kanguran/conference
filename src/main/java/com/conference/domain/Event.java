package com.conference.domain;

import com.conference.domain.enumeration.EventStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Event implements Serializable {

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
    @Column(name = "event_status")
    private EventStatus eventStatus;

    @JsonIgnoreProperties(value = { "appUser" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ApplicationUser mainHost;

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contextHost", "eventContextRegistrations", "event" }, allowSetters = true)
    private Set<EventContext> eventContexts = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Event id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Event name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventStatus getEventStatus() {
        return this.eventStatus;
    }

    public Event eventStatus(EventStatus eventStatus) {
        this.setEventStatus(eventStatus);
        return this;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public ApplicationUser getMainHost() {
        return this.mainHost;
    }

    public void setMainHost(ApplicationUser applicationUser) {
        this.mainHost = applicationUser;
    }

    public Event mainHost(ApplicationUser applicationUser) {
        this.setMainHost(applicationUser);
        return this;
    }

    public Set<EventContext> getEventContexts() {
        return this.eventContexts;
    }

    public void setEventContexts(Set<EventContext> eventContexts) {
        if (this.eventContexts != null) {
            this.eventContexts.forEach(i -> i.setEvent(null));
        }
        if (eventContexts != null) {
            eventContexts.forEach(i -> i.setEvent(this));
        }
        this.eventContexts = eventContexts;
    }

    public Event eventContexts(Set<EventContext> eventContexts) {
        this.setEventContexts(eventContexts);
        return this;
    }

    public Event addEventContext(EventContext eventContext) {
        this.eventContexts.add(eventContext);
        eventContext.setEvent(this);
        return this;
    }

    public Event removeEventContext(EventContext eventContext) {
        this.eventContexts.remove(eventContext);
        eventContext.setEvent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", eventStatus='" + getEventStatus() + "'" +
            "}";
    }
}
