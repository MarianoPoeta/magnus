package com.magnus.service.mapper;

import static com.magnus.domain.ShoppingItemAsserts.*;
import static com.magnus.domain.ShoppingItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShoppingItemMapperTest {

    private ShoppingItemMapper shoppingItemMapper;

    @BeforeEach
    void setUp() {
        shoppingItemMapper = new ShoppingItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getShoppingItemSample1();
        var actual = shoppingItemMapper.toEntity(shoppingItemMapper.toDto(expected));
        assertShoppingItemAllPropertiesEquals(expected, actual);
    }
}
