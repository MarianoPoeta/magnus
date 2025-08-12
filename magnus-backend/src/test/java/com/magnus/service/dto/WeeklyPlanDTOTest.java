package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WeeklyPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WeeklyPlanDTO.class);
        WeeklyPlanDTO weeklyPlanDTO1 = new WeeklyPlanDTO();
        weeklyPlanDTO1.setId(1L);
        WeeklyPlanDTO weeklyPlanDTO2 = new WeeklyPlanDTO();
        assertThat(weeklyPlanDTO1).isNotEqualTo(weeklyPlanDTO2);
        weeklyPlanDTO2.setId(weeklyPlanDTO1.getId());
        assertThat(weeklyPlanDTO1).isEqualTo(weeklyPlanDTO2);
        weeklyPlanDTO2.setId(2L);
        assertThat(weeklyPlanDTO1).isNotEqualTo(weeklyPlanDTO2);
        weeklyPlanDTO1.setId(null);
        assertThat(weeklyPlanDTO1).isNotEqualTo(weeklyPlanDTO2);
    }
}
