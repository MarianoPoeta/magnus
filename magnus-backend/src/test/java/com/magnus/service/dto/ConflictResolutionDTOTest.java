package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConflictResolutionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConflictResolutionDTO.class);
        ConflictResolutionDTO conflictResolutionDTO1 = new ConflictResolutionDTO();
        conflictResolutionDTO1.setId(1L);
        ConflictResolutionDTO conflictResolutionDTO2 = new ConflictResolutionDTO();
        assertThat(conflictResolutionDTO1).isNotEqualTo(conflictResolutionDTO2);
        conflictResolutionDTO2.setId(conflictResolutionDTO1.getId());
        assertThat(conflictResolutionDTO1).isEqualTo(conflictResolutionDTO2);
        conflictResolutionDTO2.setId(2L);
        assertThat(conflictResolutionDTO1).isNotEqualTo(conflictResolutionDTO2);
        conflictResolutionDTO1.setId(null);
        assertThat(conflictResolutionDTO1).isNotEqualTo(conflictResolutionDTO2);
    }
}
