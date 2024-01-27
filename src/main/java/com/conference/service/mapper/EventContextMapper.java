package com.conference.service.mapper;

import com.conference.domain.ApplicationUser;
import com.conference.domain.Event;
import com.conference.domain.EventContext;
import com.conference.domain.Room;
import com.conference.service.dto.ApplicationUserDTO;
import com.conference.service.dto.EventContextDTO;
import com.conference.service.dto.EventDTO;
import com.conference.service.dto.RoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventContext} and its DTO {@link EventContextDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventContextMapper extends EntityMapper<EventContextDTO, EventContext> {
    @Mapping(target = "eventContextRoom", source = "eventContextRoom", qualifiedByName = "roomId")
    @Mapping(target = "contextHost", source = "contextHost", qualifiedByName = "applicationUserId")
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    EventContextDTO toDto(EventContext s);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    RoomDTO toDtoRoomId(Room room);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "appUser", source = "appUser")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EventDTO toDtoEventId(Event event);
}
