package com.magnus.service.mapper;

import com.magnus.domain.FoodItem;
import com.magnus.domain.Menu;
import com.magnus.service.dto.FoodItemDTO;
import com.magnus.service.dto.MenuDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Menu} and its DTO {@link MenuDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuMapper extends EntityMapper<MenuDTO, Menu> {
    @Mapping(target = "includedFoodItems", source = "includedFoodItems", qualifiedByName = "foodItemIdSet")
    MenuDTO toDto(Menu s);

    @Mapping(target = "removeIncludedFoodItems", ignore = true)
    Menu toEntity(MenuDTO menuDTO);

    @Named("foodItemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FoodItemDTO toDtoFoodItemId(FoodItem foodItem);

    @Named("foodItemIdSet")
    default Set<FoodItemDTO> toDtoFoodItemIdSet(Set<FoodItem> foodItem) {
        return foodItem.stream().map(this::toDtoFoodItemId).collect(Collectors.toSet());
    }
}
