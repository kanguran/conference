package com.conference.service.dto;

import com.conference.domain.enumeration.EventRegistrationStatus;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.conference.domain.EventRegistration} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventRegistrationDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private EventRegistrationStatus eventRegistrationStatus;

    private UserDTO eventCounterparty;

    private EventContextDTO eventContext;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventRegistrationStatus getEventRegistrationStatus() {
        return eventRegistrationStatus;
    }

    public void setEventRegistrationStatus(EventRegistrationStatus eventRegistrationStatus) {
        this.eventRegistrationStatus = eventRegistrationStatus;
    }

    public UserDTO getEventCounterparty() {
        return eventCounterparty;
    }

    public void setEventCounterparty(UserDTO eventCounterparty) {
        this.eventCounterparty = eventCounterparty;
    }

    public EventContextDTO getEventContext() {
        return eventContext;
    }

    public void setEventContext(EventContextDTO eventContext) {
        this.eventContext = eventContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventRegistrationDTO)) {
            return false;
        }

        EventRegistrationDTO eventRegistrationDTO = (EventRegistrationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eventRegistrationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventRegistrationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", eventRegistrationStatus='" + getEventRegistrationStatus() + "'" +
            ", eventCounterparty=" + getEventCounterparty() +
            ", eventContext=" + getEventContext() +
            "}";
    }
}
