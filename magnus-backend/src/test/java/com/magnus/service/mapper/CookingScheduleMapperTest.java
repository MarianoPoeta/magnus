package com.magnus.service.mapper;

import static com.magnus.domain.CookingScheduleAsserts.*;
import static com.magnus.domain.CookingScheduleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CookingScheduleMapperTest {

    private CookingScheduleMapper cookingScheduleMapper;

    @BeforeEach
    void setUp() {
        cookingScheduleMapper = new CookingScheduleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCookingScheduleSample1();
        var actual = cookingScheduleMapper.toEntity(cookingScheduleMapper.toDto(expected));
        assertCookingScheduleAllPropertiesEquals(expected, actual);
    }
}
