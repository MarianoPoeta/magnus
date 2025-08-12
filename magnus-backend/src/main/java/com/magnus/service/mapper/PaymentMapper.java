package com.magnus.service.mapper;

import com.magnus.domain.Budget;
import com.magnus.domain.Payment;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "budget", source = "budget", qualifiedByName = "budgetId")
    PaymentDTO toDto(Payment s);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);
}
