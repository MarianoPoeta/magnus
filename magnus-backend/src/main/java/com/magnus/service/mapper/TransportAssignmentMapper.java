package com.magnus.service.mapper;

import com.magnus.domain.Activity;
import com.magnus.domain.Budget;
import com.magnus.domain.Transport;
import com.magnus.domain.TransportAssignment;
import com.magnus.service.dto.ActivityDTO;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.dto.TransportAssignmentDTO;
import com.magnus.service.dto.TransportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransportAssignment} and its DTO {@link TransportAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransportAssignmentMapper extends EntityMapper<TransportAssignmentDTO, TransportAssignment> {
    @Mapping(target = "transport", source = "transport", qualifiedByName = "transportId")
    @Mapping(target = "budget", source = "budget", qualifiedByName = "budgetId")
    @Mapping(target = "activity", source = "activity", qualifiedByName = "activityId")
    TransportAssignmentDTO toDto(TransportAssignment s);

    @Named("transportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransportDTO toDtoTransportId(Transport transport);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);

    @Named("activityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActivityDTO toDtoActivityId(Activity activity);
}
