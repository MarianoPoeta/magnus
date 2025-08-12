package com.magnus.service.mapper;

import static com.magnus.domain.MenuItemAsserts.*;
import static com.magnus.domain.MenuItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuItemMapperTest {

    private MenuItemMapper menuItemMapper;

    @BeforeEach
    void setUp() {
        menuItemMapper = new MenuItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMenuItemSample1();
        var actual = menuItemMapper.toEntity(menuItemMapper.toDto(expected));
        assertMenuItemAllPropertiesEquals(expected, actual);
    }
}
