package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.Budget;
import com.magnus.domain.BudgetTemplate;
import com.magnus.domain.Client;
import com.magnus.domain.WeeklyPlan;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.dto.BudgetTemplateDTO;
import com.magnus.service.dto.ClientDTO;
import com.magnus.service.dto.WeeklyPlanDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Budget} and its DTO {@link BudgetDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetMapper extends EntityMapper<BudgetDTO, Budget> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "appUserId")
    @Mapping(target = "assignedTo", source = "assignedTo", qualifiedByName = "appUserId")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    @Mapping(target = "template", source = "template", qualifiedByName = "budgetTemplateId")
    @Mapping(target = "weeklyPlan", source = "weeklyPlan", qualifiedByName = "weeklyPlanId")
    BudgetDTO toDto(Budget s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);

    @Named("budgetTemplateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetTemplateDTO toDtoBudgetTemplateId(BudgetTemplate budgetTemplate);

    @Named("weeklyPlanId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    WeeklyPlanDTO toDtoWeeklyPlanId(WeeklyPlan weeklyPlan);
}
