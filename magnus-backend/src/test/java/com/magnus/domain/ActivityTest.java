package com.magnus.domain;

import static com.magnus.domain.ActivityTestSamples.*;
import static com.magnus.domain.ProductRequirementTestSamples.*;
import static com.magnus.domain.TransportAssignmentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Activity.class);
        Activity activity1 = getActivitySample1();
        Activity activity2 = new Activity();
        assertThat(activity1).isNotEqualTo(activity2);

        activity2.setId(activity1.getId());
        assertThat(activity1).isEqualTo(activity2);

        activity2 = getActivitySample2();
        assertThat(activity1).isNotEqualTo(activity2);
    }

    @Test
    void productRequirementTest() {
        Activity activity = getActivityRandomSampleGenerator();
        ProductRequirement productRequirementBack = getProductRequirementRandomSampleGenerator();

        activity.addProductRequirement(productRequirementBack);
        assertThat(activity.getProductRequirements()).containsOnly(productRequirementBack);
        assertThat(productRequirementBack.getActivity()).isEqualTo(activity);

        activity.removeProductRequirement(productRequirementBack);
        assertThat(activity.getProductRequirements()).doesNotContain(productRequirementBack);
        assertThat(productRequirementBack.getActivity()).isNull();

        activity.productRequirements(new HashSet<>(Set.of(productRequirementBack)));
        assertThat(activity.getProductRequirements()).containsOnly(productRequirementBack);
        assertThat(productRequirementBack.getActivity()).isEqualTo(activity);

        activity.setProductRequirements(new HashSet<>());
        assertThat(activity.getProductRequirements()).doesNotContain(productRequirementBack);
        assertThat(productRequirementBack.getActivity()).isNull();
    }

    @Test
    void transportAssignmentTest() {
        Activity activity = getActivityRandomSampleGenerator();
        TransportAssignment transportAssignmentBack = getTransportAssignmentRandomSampleGenerator();

        activity.addTransportAssignment(transportAssignmentBack);
        assertThat(activity.getTransportAssignments()).containsOnly(transportAssignmentBack);
        assertThat(transportAssignmentBack.getActivity()).isEqualTo(activity);

        activity.removeTransportAssignment(transportAssignmentBack);
        assertThat(activity.getTransportAssignments()).doesNotContain(transportAssignmentBack);
        assertThat(transportAssignmentBack.getActivity()).isNull();

        activity.transportAssignments(new HashSet<>(Set.of(transportAssignmentBack)));
        assertThat(activity.getTransportAssignments()).containsOnly(transportAssignmentBack);
        assertThat(transportAssignmentBack.getActivity()).isEqualTo(activity);

        activity.setTransportAssignments(new HashSet<>());
        assertThat(activity.getTransportAssignments()).doesNotContain(transportAssignmentBack);
        assertThat(transportAssignmentBack.getActivity()).isNull();
    }
}
