package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.ConflictResolution;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.ConflictResolutionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConflictResolution} and its DTO {@link ConflictResolutionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConflictResolutionMapper extends EntityMapper<ConflictResolutionDTO, ConflictResolution> {
    @Mapping(target = "conflictUser", source = "conflictUser", qualifiedByName = "appUserId")
    @Mapping(target = "resolvedBy", source = "resolvedBy", qualifiedByName = "appUserId")
    ConflictResolutionDTO toDto(ConflictResolution s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);
}
