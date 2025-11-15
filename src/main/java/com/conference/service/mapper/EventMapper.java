package com.conference.service.mapper;

import com.conference.domain.ApplicationUser;
import com.conference.domain.Event;
import com.conference.service.dto.ApplicationUserDTO;
import com.conference.service.dto.EventDTO;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class }, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper extends EntityMapper<EventDTO, Event> {
    @Mapping(target = "mainHost", source = "mainHost", qualifiedByName = "applicationUserId")
    EventDTO toDto(Event s);

    @Named("applicationUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "host", source = "host")
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "id")
    ApplicationUserDTO toDtoApplicationUserId(ApplicationUser applicationUser);
}
