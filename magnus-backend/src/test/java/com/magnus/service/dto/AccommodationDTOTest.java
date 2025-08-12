package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccommodationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccommodationDTO.class);
        AccommodationDTO accommodationDTO1 = new AccommodationDTO();
        accommodationDTO1.setId(1L);
        AccommodationDTO accommodationDTO2 = new AccommodationDTO();
        assertThat(accommodationDTO1).isNotEqualTo(accommodationDTO2);
        accommodationDTO2.setId(accommodationDTO1.getId());
        assertThat(accommodationDTO1).isEqualTo(accommodationDTO2);
        accommodationDTO2.setId(2L);
        assertThat(accommodationDTO1).isNotEqualTo(accommodationDTO2);
        accommodationDTO1.setId(null);
        assertThat(accommodationDTO1).isNotEqualTo(accommodationDTO2);
    }
}
