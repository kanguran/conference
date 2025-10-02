package com.conference.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "host")
    private Boolean host;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User appUser;

    @JsonIgnoreProperties(value = { "mainHost", "eventContexts" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "mainHost")
    private Event event;

    @JsonIgnoreProperties(value = { "eventContextRoom", "contextHost", "eventContextRegistrations", "event" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "contextHost")
    private EventContext eventContext;

    @JsonIgnoreProperties(value = { "eventCounterparty", "eventContext" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "eventCounterparty")
    private EventRegistration eventRegistration;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getHost() {
        return this.host;
    }

    public ApplicationUser host(Boolean host) {
        this.setHost(host);
        return this;
    }

    public void setHost(Boolean host) {
        this.host = host;
    }

    public User getAppUser() {
        return this.appUser;
    }

    public void setAppUser(User user) {
        this.appUser = user;
    }

    public ApplicationUser appUser(User user) {
        this.setAppUser(user);
        return this;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        if (this.event != null) {
            this.event.setMainHost(null);
        }
        if (event != null) {
            event.setMainHost(this);
        }
        this.event = event;
    }

    public ApplicationUser event(Event event) {
        this.setEvent(event);
        return this;
    }

    public EventContext getEventContext() {
        return this.eventContext;
    }

    public void setEventContext(EventContext eventContext) {
        if (this.eventContext != null) {
            this.eventContext.setContextHost(null);
        }
        if (eventContext != null) {
            eventContext.setContextHost(this);
        }
        this.eventContext = eventContext;
    }

    public ApplicationUser eventContext(EventContext eventContext) {
        this.setEventContext(eventContext);
        return this;
    }

    public EventRegistration getEventRegistration() {
        return this.eventRegistration;
    }

    public void setEventRegistration(EventRegistration eventRegistration) {
        if (this.eventRegistration != null) {
            this.eventRegistration.setEventCounterparty(null);
        }
        if (eventRegistration != null) {
            eventRegistration.setEventCounterparty(this);
        }
        this.eventRegistration = eventRegistration;
    }

    public ApplicationUser eventRegistration(EventRegistration eventRegistration) {
        this.setEventRegistration(eventRegistration);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", host='" + getHost() + "'" +
            "}";
    }
}
