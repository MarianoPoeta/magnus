package com.magnus.service.mapper;

import static com.magnus.domain.ClientAsserts.*;
import static com.magnus.domain.ClientTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientMapperTest {

    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClientSample1();
        var actual = clientMapper.toEntity(clientMapper.toDto(expected));
        assertClientAllPropertiesEquals(expected, actual);
    }
}
