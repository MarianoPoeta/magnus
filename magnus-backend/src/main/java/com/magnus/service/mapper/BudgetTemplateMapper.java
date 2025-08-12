package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.BudgetTemplate;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.BudgetTemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BudgetTemplate} and its DTO {@link BudgetTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetTemplateMapper extends EntityMapper<BudgetTemplateDTO, BudgetTemplate> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "appUserId")
    BudgetTemplateDTO toDto(BudgetTemplate s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);
}
