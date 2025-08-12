package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.WorkflowTriggerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkflowTriggerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkflowTrigger.class);
        WorkflowTrigger workflowTrigger1 = getWorkflowTriggerSample1();
        WorkflowTrigger workflowTrigger2 = new WorkflowTrigger();
        assertThat(workflowTrigger1).isNotEqualTo(workflowTrigger2);

        workflowTrigger2.setId(workflowTrigger1.getId());
        assertThat(workflowTrigger1).isEqualTo(workflowTrigger2);

        workflowTrigger2 = getWorkflowTriggerSample2();
        assertThat(workflowTrigger1).isNotEqualTo(workflowTrigger2);
    }

    @Test
    void createdByTest() {
        WorkflowTrigger workflowTrigger = getWorkflowTriggerRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        workflowTrigger.setCreatedBy(appUserBack);
        assertThat(workflowTrigger.getCreatedBy()).isEqualTo(appUserBack);

        workflowTrigger.createdBy(null);
        assertThat(workflowTrigger.getCreatedBy()).isNull();
    }
}
