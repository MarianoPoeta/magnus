package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.WeeklyPlan;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.WeeklyPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WeeklyPlan} and its DTO {@link WeeklyPlanDTO}.
 */
@Mapper(componentModel = "spring")
public interface WeeklyPlanMapper extends EntityMapper<WeeklyPlanDTO, WeeklyPlan> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "appUserId")
    WeeklyPlanDTO toDto(WeeklyPlan s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);
}
