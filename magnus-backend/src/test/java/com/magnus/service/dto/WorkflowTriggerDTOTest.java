package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowTriggerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowTriggerDTO.class);
        WorkflowTriggerDTO workflowTriggerDTO1 = new WorkflowTriggerDTO();
        workflowTriggerDTO1.setId(1L);
        WorkflowTriggerDTO workflowTriggerDTO2 = new WorkflowTriggerDTO();
        assertThat(workflowTriggerDTO1).isNotEqualTo(workflowTriggerDTO2);
        workflowTriggerDTO2.setId(workflowTriggerDTO1.getId());
        assertThat(workflowTriggerDTO1).isEqualTo(workflowTriggerDTO2);
        workflowTriggerDTO2.setId(2L);
        assertThat(workflowTriggerDTO1).isNotEqualTo(workflowTriggerDTO2);
        workflowTriggerDTO1.setId(null);
        assertThat(workflowTriggerDTO1).isNotEqualTo(workflowTriggerDTO2);
    }
}
