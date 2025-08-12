package com.magnus.service.mapper;

import static com.magnus.domain.TaskDependencyAsserts.*;
import static com.magnus.domain.TaskDependencyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskDependencyMapperTest {

    private TaskDependencyMapper taskDependencyMapper;

    @BeforeEach
    void setUp() {
        taskDependencyMapper = new TaskDependencyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTaskDependencySample1();
        var actual = taskDependencyMapper.toEntity(taskDependencyMapper.toDto(expected));
        assertTaskDependencyAllPropertiesEquals(expected, actual);
    }
}
