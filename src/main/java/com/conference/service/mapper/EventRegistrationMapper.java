package com.conference.service.mapper;

import com.conference.domain.EventContext;
import com.conference.domain.EventRegistration;
import com.conference.domain.User;
import com.conference.service.dto.EventContextDTO;
import com.conference.service.dto.EventRegistrationDTO;
import com.conference.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventRegistration} and its DTO {@link EventRegistrationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventRegistrationMapper extends EntityMapper<EventRegistrationDTO, EventRegistration> {
    @Mapping(target = "eventCounterparty", source = "eventCounterparty", qualifiedByName = "userId")
    @Mapping(target = "eventContext", source = "eventContext", qualifiedByName = "eventContextId")
    EventRegistrationDTO toDto(EventRegistration s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("eventContextId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventContextDTO toDtoEventContextId(EventContext eventContext);
}
