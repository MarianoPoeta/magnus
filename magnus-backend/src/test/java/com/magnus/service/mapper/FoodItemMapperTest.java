package com.magnus.service.mapper;

import static com.magnus.domain.FoodItemAsserts.*;
import static com.magnus.domain.FoodItemTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FoodItemMapperTest {

    private FoodItemMapper foodItemMapper;

    @BeforeEach
    void setUp() {
        foodItemMapper = new FoodItemMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFoodItemSample1();
        var actual = foodItemMapper.toEntity(foodItemMapper.toDto(expected));
        assertFoodItemAllPropertiesEquals(expected, actual);
    }
}
