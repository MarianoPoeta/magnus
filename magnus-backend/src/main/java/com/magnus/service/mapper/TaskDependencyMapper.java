package com.magnus.service.mapper;

import com.magnus.domain.Task;
import com.magnus.domain.TaskDependency;
import com.magnus.service.dto.TaskDTO;
import com.magnus.service.dto.TaskDependencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskDependency} and its DTO {@link TaskDependencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskDependencyMapper extends EntityMapper<TaskDependencyDTO, TaskDependency> {
    @Mapping(target = "prerequisiteTask", source = "prerequisiteTask", qualifiedByName = "taskId")
    @Mapping(target = "dependentTask", source = "dependentTask", qualifiedByName = "taskId")
    TaskDependencyDTO toDto(TaskDependency s);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);
}
