package com.conference.service.mapper;

import com.conference.domain.Event;
import com.conference.domain.User;
import com.conference.service.dto.EventDTO;
import com.conference.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventMapper extends EntityMapper<EventDTO, Event> {
    @Mapping(target = "mainHost", source = "mainHost", qualifiedByName = "userId")
    EventDTO toDto(Event s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
