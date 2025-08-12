package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransportAssignmentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransportAssignmentDTO.class);
        TransportAssignmentDTO transportAssignmentDTO1 = new TransportAssignmentDTO();
        transportAssignmentDTO1.setId(1L);
        TransportAssignmentDTO transportAssignmentDTO2 = new TransportAssignmentDTO();
        assertThat(transportAssignmentDTO1).isNotEqualTo(transportAssignmentDTO2);
        transportAssignmentDTO2.setId(transportAssignmentDTO1.getId());
        assertThat(transportAssignmentDTO1).isEqualTo(transportAssignmentDTO2);
        transportAssignmentDTO2.setId(2L);
        assertThat(transportAssignmentDTO1).isNotEqualTo(transportAssignmentDTO2);
        transportAssignmentDTO1.setId(null);
        assertThat(transportAssignmentDTO1).isNotEqualTo(transportAssignmentDTO2);
    }
}
