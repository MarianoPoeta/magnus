package com.magnus.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CookingIngredientDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CookingIngredientDTO.class);
        CookingIngredientDTO cookingIngredientDTO1 = new CookingIngredientDTO();
        cookingIngredientDTO1.setId(1L);
        CookingIngredientDTO cookingIngredientDTO2 = new CookingIngredientDTO();
        assertThat(cookingIngredientDTO1).isNotEqualTo(cookingIngredientDTO2);
        cookingIngredientDTO2.setId(cookingIngredientDTO1.getId());
        assertThat(cookingIngredientDTO1).isEqualTo(cookingIngredientDTO2);
        cookingIngredientDTO2.setId(2L);
        assertThat(cookingIngredientDTO1).isNotEqualTo(cookingIngredientDTO2);
        cookingIngredientDTO1.setId(null);
        assertThat(cookingIngredientDTO1).isNotEqualTo(cookingIngredientDTO2);
    }
}
