package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.ShoppingItem;
import com.magnus.domain.WeeklyPlan;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.ShoppingItemDTO;
import com.magnus.service.dto.WeeklyPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShoppingItem} and its DTO {@link ShoppingItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShoppingItemMapper extends EntityMapper<ShoppingItemDTO, ShoppingItem> {
    @Mapping(target = "purchasedBy", source = "purchasedBy", qualifiedByName = "appUserId")
    @Mapping(target = "weeklyPlan", source = "weeklyPlan", qualifiedByName = "weeklyPlanId")
    ShoppingItemDTO toDto(ShoppingItem s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("weeklyPlanId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WeeklyPlanDTO toDtoWeeklyPlanId(WeeklyPlan weeklyPlan);
}
