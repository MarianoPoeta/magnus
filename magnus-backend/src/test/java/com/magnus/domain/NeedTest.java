package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.NeedTestSamples.*;
import static com.magnus.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NeedTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Need.class);
        Need need1 = getNeedSample1();
        Need need2 = new Need();
        assertThat(need1).isNotEqualTo(need2);

        need2.setId(need1.getId());
        assertThat(need1).isEqualTo(need2);

        need2 = getNeedSample2();
        assertThat(need1).isNotEqualTo(need2);
    }

    @Test
    void requestedByTest() {
        Need need = getNeedRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        need.setRequestedBy(appUserBack);
        assertThat(need.getRequestedBy()).isEqualTo(appUserBack);

        need.requestedBy(null);
        assertThat(need.getRequestedBy()).isNull();
    }

    @Test
    void fulfilledByTest() {
        Need need = getNeedRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        need.setFulfilledBy(appUserBack);
        assertThat(need.getFulfilledBy()).isEqualTo(appUserBack);

        need.fulfilledBy(null);
        assertThat(need.getFulfilledBy()).isNull();
    }

    @Test
    void parentTaskTest() {
        Need need = getNeedRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        need.setParentTask(taskBack);
        assertThat(need.getParentTask()).isEqualTo(taskBack);

        need.parentTask(null);
        assertThat(need.getParentTask()).isNull();
    }
}
