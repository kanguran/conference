package com.conference.service.mapper;

import com.conference.domain.EventContext;
import com.conference.domain.Room;
import com.conference.service.dto.EventContextDTO;
import com.conference.service.dto.RoomDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Room} and its DTO {@link RoomDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoomMapper extends EntityMapper<RoomDTO, Room> {
    @Mapping(target = "roomEventContext", source = "roomEventContext", qualifiedByName = "eventContextId")
    RoomDTO toDto(Room s);

    @Named("eventContextId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventContextDTO toDtoEventContextId(EventContext eventContext);
}
