package com.magnus.service.mapper;

import static com.magnus.domain.WeeklyPlanAsserts.*;
import static com.magnus.domain.WeeklyPlanTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WeeklyPlanMapperTest {

    private WeeklyPlanMapper weeklyPlanMapper;

    @BeforeEach
    void setUp() {
        weeklyPlanMapper = new WeeklyPlanMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWeeklyPlanSample1();
        var actual = weeklyPlanMapper.toEntity(weeklyPlanMapper.toDto(expected));
        assertWeeklyPlanAllPropertiesEquals(expected, actual);
    }
}
