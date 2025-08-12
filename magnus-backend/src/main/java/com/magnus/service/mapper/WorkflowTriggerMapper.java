package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.WorkflowTrigger;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.WorkflowTriggerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkflowTrigger} and its DTO {@link WorkflowTriggerDTO}.
 */
@Mapper(componentModel = "spring")
public interface WorkflowTriggerMapper extends EntityMapper<WorkflowTriggerDTO, WorkflowTrigger> {
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "appUserId")
    WorkflowTriggerDTO toDto(WorkflowTrigger s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);
}
