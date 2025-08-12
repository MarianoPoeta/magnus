package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.AuditLogTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AuditLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuditLog.class);
        AuditLog auditLog1 = getAuditLogSample1();
        AuditLog auditLog2 = new AuditLog();
        assertThat(auditLog1).isNotEqualTo(auditLog2);

        auditLog2.setId(auditLog1.getId());
        assertThat(auditLog1).isEqualTo(auditLog2);

        auditLog2 = getAuditLogSample2();
        assertThat(auditLog1).isNotEqualTo(auditLog2);
    }

    @Test
    void userTest() {
        AuditLog auditLog = getAuditLogRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        auditLog.setUser(appUserBack);
        assertThat(auditLog.getUser()).isEqualTo(appUserBack);

        auditLog.user(null);
        assertThat(auditLog.getUser()).isNull();
    }
}
