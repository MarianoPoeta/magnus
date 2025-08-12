package com.magnus.service.mapper;

import static com.magnus.domain.WorkflowTriggerAsserts.*;
import static com.magnus.domain.WorkflowTriggerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WorkflowTriggerMapperTest {

    private WorkflowTriggerMapper workflowTriggerMapper;

    @BeforeEach
    void setUp() {
        workflowTriggerMapper = new WorkflowTriggerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getWorkflowTriggerSample1();
        var actual = workflowTriggerMapper.toEntity(workflowTriggerMapper.toDto(expected));
        assertWorkflowTriggerAllPropertiesEquals(expected, actual);
    }
}
