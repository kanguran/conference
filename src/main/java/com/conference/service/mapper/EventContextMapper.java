package com.conference.service.mapper;

import com.conference.domain.Event;
import com.conference.domain.EventContext;
import com.conference.domain.User;
import com.conference.service.dto.EventContextDTO;
import com.conference.service.dto.EventDTO;
import com.conference.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventContext} and its DTO {@link EventContextDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventContextMapper extends EntityMapper<EventContextDTO, EventContext> {
    @Mapping(target = "contextHost", source = "contextHost", qualifiedByName = "userId")
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    EventContextDTO toDto(EventContext s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);
}
