package com.conference.service.mapper;

import com.conference.domain.ApplicationUser;
import com.conference.domain.Event;
import com.conference.service.dto.ApplicationUserDTO;
import com.conference.service.dto.EventDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventMapper extends EntityMapper<EventDTO, Event> {
    @Mapping(target = "mainHost", source = "mainHost", qualifiedByName = "applicationUserId")
    EventDTO toDto(Event s);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "appUser", source = "appUser")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);
}
