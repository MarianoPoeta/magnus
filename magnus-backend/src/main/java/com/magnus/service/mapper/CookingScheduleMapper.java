package com.magnus.service.mapper;

import com.magnus.domain.Budget;
import com.magnus.domain.CookingSchedule;
import com.magnus.domain.Task;
import com.magnus.service.dto.BudgetDTO;
import com.magnus.service.dto.CookingScheduleDTO;
import com.magnus.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CookingSchedule} and its DTO {@link CookingScheduleDTO}.
 */
@Mapper(componentModel = "spring")
public interface CookingScheduleMapper extends EntityMapper<CookingScheduleDTO, CookingSchedule> {
    @Mapping(target = "relatedTask", source = "relatedTask", qualifiedByName = "taskId")
    @Mapping(target = "budget", source = "budget", qualifiedByName = "budgetId")
    CookingScheduleDTO toDto(CookingSchedule s);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);

    @Named("budgetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BudgetDTO toDtoBudgetId(Budget budget);
}
