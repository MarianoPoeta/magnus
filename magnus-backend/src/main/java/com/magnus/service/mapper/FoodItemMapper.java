package com.magnus.service.mapper;

import com.magnus.domain.FoodItem;
import com.magnus.domain.Menu;
import com.magnus.service.dto.FoodItemDTO;
import com.magnus.service.dto.MenuDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FoodItem} and its DTO {@link FoodItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface FoodItemMapper extends EntityMapper<FoodItemDTO, FoodItem> {
    @Mapping(target = "availableMenus", source = "availableMenus", qualifiedByName = "menuIdSet")
    FoodItemDTO toDto(FoodItem s);

    @Mapping(target = "availableMenus", ignore = true)
    @Mapping(target = "removeAvailableMenus", ignore = true)
    FoodItem toEntity(FoodItemDTO foodItemDTO);

    @Named("menuId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuDTO toDtoMenuId(Menu menu);

    @Named("menuIdSet")
    default Set<MenuDTO> toDtoMenuIdSet(Set<Menu> menu) {
        return menu.stream().map(this::toDtoMenuId).collect(Collectors.toSet());
    }
}
