package com.magnus.service.mapper;

import static com.magnus.domain.SystemConfigAsserts.*;
import static com.magnus.domain.SystemConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemConfigMapperTest {

    private SystemConfigMapper systemConfigMapper;

    @BeforeEach
    void setUp() {
        systemConfigMapper = new SystemConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSystemConfigSample1();
        var actual = systemConfigMapper.toEntity(systemConfigMapper.toDto(expected));
        assertSystemConfigAllPropertiesEquals(expected, actual);
    }
}
