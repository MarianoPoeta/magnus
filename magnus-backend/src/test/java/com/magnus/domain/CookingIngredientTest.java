package com.magnus.domain;

import static com.magnus.domain.CookingIngredientTestSamples.*;
import static com.magnus.domain.CookingScheduleTestSamples.*;
import static com.magnus.domain.ProductRequirementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CookingIngredientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CookingIngredient.class);
        CookingIngredient cookingIngredient1 = getCookingIngredientSample1();
        CookingIngredient cookingIngredient2 = new CookingIngredient();
        assertThat(cookingIngredient1).isNotEqualTo(cookingIngredient2);

        cookingIngredient2.setId(cookingIngredient1.getId());
        assertThat(cookingIngredient1).isEqualTo(cookingIngredient2);

        cookingIngredient2 = getCookingIngredientSample2();
        assertThat(cookingIngredient1).isNotEqualTo(cookingIngredient2);
    }

    @Test
    void productRequirementTest() {
        CookingIngredient cookingIngredient = getCookingIngredientRandomSampleGenerator();
        ProductRequirement productRequirementBack = getProductRequirementRandomSampleGenerator();

        cookingIngredient.setProductRequirement(productRequirementBack);
        assertThat(cookingIngredient.getProductRequirement()).isEqualTo(productRequirementBack);

        cookingIngredient.productRequirement(null);
        assertThat(cookingIngredient.getProductRequirement()).isNull();
    }

    @Test
    void cookingScheduleTest() {
        CookingIngredient cookingIngredient = getCookingIngredientRandomSampleGenerator();
        CookingSchedule cookingScheduleBack = getCookingScheduleRandomSampleGenerator();

        cookingIngredient.setCookingSchedule(cookingScheduleBack);
        assertThat(cookingIngredient.getCookingSchedule()).isEqualTo(cookingScheduleBack);

        cookingIngredient.cookingSchedule(null);
        assertThat(cookingIngredient.getCookingSchedule()).isNull();
    }
}
