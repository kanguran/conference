package com.conference.service.mapper;

import com.conference.domain.Event;
import com.conference.domain.EventContext;
import com.conference.service.dto.EventContextDTO;
import com.conference.service.dto.EventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventContext} and its DTO {@link EventContextDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventContextMapper extends EntityMapper<EventContextDTO, EventContext> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    EventContextDTO toDto(EventContext s);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);
}