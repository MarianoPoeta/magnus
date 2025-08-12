package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FoodItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodItemDTO.class);
        FoodItemDTO foodItemDTO1 = new FoodItemDTO();
        foodItemDTO1.setId(1L);
        FoodItemDTO foodItemDTO2 = new FoodItemDTO();
        assertThat(foodItemDTO1).isNotEqualTo(foodItemDTO2);
        foodItemDTO2.setId(foodItemDTO1.getId());
        assertThat(foodItemDTO1).isEqualTo(foodItemDTO2);
        foodItemDTO2.setId(2L);
        assertThat(foodItemDTO1).isNotEqualTo(foodItemDTO2);
        foodItemDTO1.setId(null);
        assertThat(foodItemDTO1).isNotEqualTo(foodItemDTO2);
    }
}
