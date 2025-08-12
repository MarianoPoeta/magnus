package com.magnus.service.mapper;

import com.magnus.domain.Budget;
import com.magnus.domain.BudgetItem;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.dto.BudgetItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BudgetItem} and its DTO {@link BudgetItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface BudgetItemMapper extends EntityMapper<BudgetItemDTO, BudgetItem> {
    @Mapping(target = "budget", source = "budget", qualifiedByName = "budgetId")
    BudgetItemDTO toDto(BudgetItem s);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);
}
