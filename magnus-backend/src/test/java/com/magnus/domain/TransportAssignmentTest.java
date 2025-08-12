package com.magnus.domain;

import static com.magnus.domain.ActivityTestSamples.*;
import static com.magnus.domain.BudgetTestSamples.*;
import static com.magnus.domain.TransportAssignmentTestSamples.*;
import static com.magnus.domain.TransportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransportAssignmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransportAssignment.class);
        TransportAssignment transportAssignment1 = getTransportAssignmentSample1();
        TransportAssignment transportAssignment2 = new TransportAssignment();
        assertThat(transportAssignment1).isNotEqualTo(transportAssignment2);

        transportAssignment2.setId(transportAssignment1.getId());
        assertThat(transportAssignment1).isEqualTo(transportAssignment2);

        transportAssignment2 = getTransportAssignmentSample2();
        assertThat(transportAssignment1).isNotEqualTo(transportAssignment2);
    }

    @Test
    void transportTest() {
        TransportAssignment transportAssignment = getTransportAssignmentRandomSampleGenerator();
        Transport transportBack = getTransportRandomSampleGenerator();

        transportAssignment.setTransport(transportBack);
        assertThat(transportAssignment.getTransport()).isEqualTo(transportBack);

        transportAssignment.transport(null);
        assertThat(transportAssignment.getTransport()).isNull();
    }

    @Test
    void budgetTest() {
        TransportAssignment transportAssignment = getTransportAssignmentRandomSampleGenerator();
        Budget budgetBack = getBudgetRandomSampleGenerator();

        transportAssignment.setBudget(budgetBack);
        assertThat(transportAssignment.getBudget()).isEqualTo(budgetBack);

        transportAssignment.budget(null);
        assertThat(transportAssignment.getBudget()).isNull();
    }

    @Test
    void activityTest() {
        TransportAssignment transportAssignment = getTransportAssignmentRandomSampleGenerator();
        Activity activityBack = getActivityRandomSampleGenerator();

        transportAssignment.setActivity(activityBack);
        assertThat(transportAssignment.getActivity()).isEqualTo(activityBack);

        transportAssignment.activity(null);
        assertThat(transportAssignment.getActivity()).isNull();
    }
}
