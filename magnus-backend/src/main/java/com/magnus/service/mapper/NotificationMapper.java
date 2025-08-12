package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.Notification;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "appUserId")
    @Mapping(target = "targetUser", source = "targetUser", qualifiedByName = "appUserId")
    NotificationDTO toDto(Notification s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);
}
