package com.magnus.service.mapper;

import static com.magnus.domain.CookingIngredientAsserts.*;
import static com.magnus.domain.CookingIngredientTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CookingIngredientMapperTest {

    private CookingIngredientMapper cookingIngredientMapper;

    @BeforeEach
    void setUp() {
        cookingIngredientMapper = new CookingIngredientMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCookingIngredientSample1();
        var actual = cookingIngredientMapper.toEntity(cookingIngredientMapper.toDto(expected));
        assertCookingIngredientAllPropertiesEquals(expected, actual);
    }
}
