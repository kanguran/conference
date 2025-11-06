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
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link EventContext} and its DTO {@link EventContextDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventContextMapper extends EntityMapper<EventContextDTO, EventContext> {
    @Mapping(target = "eventContextRoom", source = "eventContextRoom", qualifiedByName = "roomId")
    @Mapping(target = "contextHost", source = "contextHost", qualifiedByName = "applicationUserId")
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    EventContextDTO toDto(EventContext s);

    @Named("roomId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoomDTO toDtoRoomId(Room room);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "host", source = "host")
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);
}
