package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.Budget;
import com.magnus.domain.Task;
import com.magnus.domain.WeeklyPlan;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.dto.TaskDTO;
import com.magnus.service.dto.WeeklyPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "appUserId")
    @Mapping(target = "assignedTo", source = "assignedTo", qualifiedByName = "appUserId")
    @Mapping(target = "weeklyPlan", source = "weeklyPlan", qualifiedByName = "weeklyPlanId")
    @Mapping(target = "relatedBudget", source = "relatedBudget", qualifiedByName = "budgetId")
    TaskDTO toDto(Task s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("weeklyPlanId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WeeklyPlanDTO toDtoWeeklyPlanId(WeeklyPlan weeklyPlan);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);
}
