package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CookingScheduleDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CookingScheduleDTO.class);
        CookingScheduleDTO cookingScheduleDTO1 = new CookingScheduleDTO();
        cookingScheduleDTO1.setId(1L);
        CookingScheduleDTO cookingScheduleDTO2 = new CookingScheduleDTO();
        assertThat(cookingScheduleDTO1).isNotEqualTo(cookingScheduleDTO2);
        cookingScheduleDTO2.setId(cookingScheduleDTO1.getId());
        assertThat(cookingScheduleDTO1).isEqualTo(cookingScheduleDTO2);
        cookingScheduleDTO2.setId(2L);
        assertThat(cookingScheduleDTO1).isNotEqualTo(cookingScheduleDTO2);
        cookingScheduleDTO1.setId(null);
        assertThat(cookingScheduleDTO1).isNotEqualTo(cookingScheduleDTO2);
    }
}
