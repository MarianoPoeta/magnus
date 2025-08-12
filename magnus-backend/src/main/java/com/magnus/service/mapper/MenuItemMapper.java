package com.magnus.service.mapper;

import com.magnus.domain.Menu;
import com.magnus.domain.MenuItem;
import com.magnus.service.dto.MenuDTO;
import com.magnus.service.dto.MenuItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MenuItem} and its DTO {@link MenuItemDTO}.
 */
@Mapper(componentModel = "spring")
public interface MenuItemMapper extends EntityMapper<MenuItemDTO, MenuItem> {
    @Mapping(target = "menu", source = "menu", qualifiedByName = "menuId")
    MenuItemDTO toDto(MenuItem s);

    @Named("menuId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MenuDTO toDtoMenuId(Menu menu);
}
