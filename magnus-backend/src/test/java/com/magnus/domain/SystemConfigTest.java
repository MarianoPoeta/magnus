package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.SystemConfigTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemConfigTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemConfig.class);
        SystemConfig systemConfig1 = getSystemConfigSample1();
        SystemConfig systemConfig2 = new SystemConfig();
        assertThat(systemConfig1).isNotEqualTo(systemConfig2);

        systemConfig2.setId(systemConfig1.getId());
        assertThat(systemConfig1).isEqualTo(systemConfig2);

        systemConfig2 = getSystemConfigSample2();
        assertThat(systemConfig1).isNotEqualTo(systemConfig2);
    }

    @Test
    void createdByTest() {
        SystemConfig systemConfig = getSystemConfigRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        systemConfig.setCreatedBy(appUserBack);
        assertThat(systemConfig.getCreatedBy()).isEqualTo(appUserBack);

        systemConfig.createdBy(null);
        assertThat(systemConfig.getCreatedBy()).isNull();
    }
}
