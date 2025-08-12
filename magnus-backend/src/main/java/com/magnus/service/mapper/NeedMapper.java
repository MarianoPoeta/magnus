package com.magnus.service.mapper;

import com.magnus.domain.AppUser;
import com.magnus.domain.Need;
import com.magnus.domain.Task;
import com.magnus.service.dto.AppUserDTO;
import com.magnus.service.dto.NeedDTO;
import com.magnus.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Need} and its DTO {@link NeedDTO}.
 */
@Mapper(componentModel = "spring")
public interface NeedMapper extends EntityMapper<NeedDTO, Need> {
    @Mapping(target = "requestedBy", source = "requestedBy", qualifiedByName = "appUserId")
    @Mapping(target = "fulfilledBy", source = "fulfilledBy", qualifiedByName = "appUserId")
    @Mapping(target = "parentTask", source = "parentTask", qualifiedByName = "taskId")
    NeedDTO toDto(Need s);

    @Named("appUserId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AppUserDTO toDtoAppUserId(AppUser appUser);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);
}
