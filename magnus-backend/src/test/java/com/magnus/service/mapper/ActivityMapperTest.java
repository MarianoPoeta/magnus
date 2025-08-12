package com.magnus.service.mapper;

import static com.magnus.domain.ActivityAsserts.*;
import static com.magnus.domain.ActivityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActivityMapperTest {

    private ActivityMapper activityMapper;

    @BeforeEach
    void setUp() {
        activityMapper = new ActivityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getActivitySample1();
        var actual = activityMapper.toEntity(activityMapper.toDto(expected));
        assertActivityAllPropertiesEquals(expected, actual);
    }
}
