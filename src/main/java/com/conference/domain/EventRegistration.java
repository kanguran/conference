package com.conference.domain;

import com.conference.domain.enumeration.EventRegistrationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventRegistration.
 */
@Entity
@Table(name = "event_registration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventRegistration implements Serializable {

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
    @Column(name = "event_registration_status")
    private EventRegistrationStatus eventRegistrationStatus;

    @OneToOne
    @JoinColumn(unique = true)
    private User eventCounterparty;

    @ManyToOne
    @JsonIgnoreProperties(value = { "contextHost", "eventContextRegistrations", "event" }, allowSetters = true)
    private EventContext eventContext;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventRegistration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public EventRegistration name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventRegistrationStatus getEventRegistrationStatus() {
        return this.eventRegistrationStatus;
    }

    public EventRegistration eventRegistrationStatus(EventRegistrationStatus eventRegistrationStatus) {
        this.setEventRegistrationStatus(eventRegistrationStatus);
        return this;
    }

    public void setEventRegistrationStatus(EventRegistrationStatus eventRegistrationStatus) {
        this.eventRegistrationStatus = eventRegistrationStatus;
    }

    public User getEventCounterparty() {
        return this.eventCounterparty;
    }

    public void setEventCounterparty(User user) {
        this.eventCounterparty = user;
    }

    public EventRegistration eventCounterparty(User user) {
        this.setEventCounterparty(user);
        return this;
    }

    public EventContext getEventContext() {
        return this.eventContext;
    }

    public void setEventContext(EventContext eventContext) {
        this.eventContext = eventContext;
    }

    public EventRegistration eventContext(EventContext eventContext) {
        this.setEventContext(eventContext);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventRegistration)) {
            return false;
        }
        return id != null && id.equals(((EventRegistration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventRegistration{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", eventRegistrationStatus='" + getEventRegistrationStatus() + "'" +
            "}";
    }
}
