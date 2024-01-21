package com.conference.service.mapper;

import com.conference.domain.ApplicationUser;
import com.conference.domain.User;
import com.conference.service.dto.ApplicationUserDTO;
import com.conference.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApplicationUser} and its DTO {@link ApplicationUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApplicationUserMapper extends EntityMapper<ApplicationUserDTO, ApplicationUser> {
    @Mapping(target = "appUser", source = "appUser", qualifiedByName = "userId")
    ApplicationUserDTO toDto(ApplicationUser s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    UserDTO toDtoUserId(User user);
}
