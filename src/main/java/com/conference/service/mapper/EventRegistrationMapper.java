package com.conference.service.mapper;

import com.conference.domain.EventContext;
import com.conference.domain.EventRegistration;
import com.conference.service.dto.EventContextDTO;
import com.conference.service.dto.EventRegistrationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventRegistration} and its DTO {@link EventRegistrationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventRegistrationMapper extends EntityMapper<EventRegistrationDTO, EventRegistration> {
    @Mapping(target = "eventContext", source = "eventContext", qualifiedByName = "eventContextId")
    EventRegistrationDTO toDto(EventRegistration s);

    @Named("eventContextId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventContextDTO toDtoEventContextId(EventContext eventContext);
}
