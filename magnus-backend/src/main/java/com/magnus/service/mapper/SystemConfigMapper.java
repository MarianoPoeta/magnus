package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.SystemConfig;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.SystemConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemConfig} and its DTO {@link SystemConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemConfigMapper extends EntityMapper<SystemConfigDTO, SystemConfig> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "appUserId")
    SystemConfigDTO toDto(SystemConfig s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);
}
