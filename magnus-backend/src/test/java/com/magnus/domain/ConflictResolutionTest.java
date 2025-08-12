package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.ConflictResolutionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConflictResolutionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConflictResolution.class);
        ConflictResolution conflictResolution1 = getConflictResolutionSample1();
        ConflictResolution conflictResolution2 = new ConflictResolution();
        assertThat(conflictResolution1).isNotEqualTo(conflictResolution2);

        conflictResolution2.setId(conflictResolution1.getId());
        assertThat(conflictResolution1).isEqualTo(conflictResolution2);

        conflictResolution2 = getConflictResolutionSample2();
        assertThat(conflictResolution1).isNotEqualTo(conflictResolution2);
    }

    @Test
    void conflictUserTest() {
        ConflictResolution conflictResolution = getConflictResolutionRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        conflictResolution.setConflictUser(appUserBack);
        assertThat(conflictResolution.getConflictUser()).isEqualTo(appUserBack);

        conflictResolution.conflictUser(null);
        assertThat(conflictResolution.getConflictUser()).isNull();
    }

    @Test
    void resolvedByTest() {
        ConflictResolution conflictResolution = getConflictResolutionRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        conflictResolution.setResolvedBy(appUserBack);
        assertThat(conflictResolution.getResolvedBy()).isEqualTo(appUserBack);

        conflictResolution.resolvedBy(null);
        assertThat(conflictResolution.getResolvedBy()).isNull();
    }
}
