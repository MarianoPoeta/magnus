package com.magnus.domain;

import static com.magnus.domain.ActivityTestSamples.*;
import static com.magnus.domain.FoodItemTestSamples.*;
import static com.magnus.domain.ProductRequirementTestSamples.*;
import static com.magnus.domain.ProductTestSamples.*;
import static com.magnus.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductRequirementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductRequirement.class);
        ProductRequirement productRequirement1 = getProductRequirementSample1();
        ProductRequirement productRequirement2 = new ProductRequirement();
        assertThat(productRequirement1).isNotEqualTo(productRequirement2);

        productRequirement2.setId(productRequirement1.getId());
        assertThat(productRequirement1).isEqualTo(productRequirement2);

        productRequirement2 = getProductRequirementSample2();
        assertThat(productRequirement1).isNotEqualTo(productRequirement2);
    }

    @Test
    void productTest() {
        ProductRequirement productRequirement = getProductRequirementRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        productRequirement.setProduct(productBack);
        assertThat(productRequirement.getProduct()).isEqualTo(productBack);

        productRequirement.product(null);
        assertThat(productRequirement.getProduct()).isNull();
    }

    @Test
    void relatedTaskTest() {
        ProductRequirement productRequirement = getProductRequirementRandomSampleGenerator();
        Task taskBack = getTaskRandomSampleGenerator();

        productRequirement.setRelatedTask(taskBack);
        assertThat(productRequirement.getRelatedTask()).isEqualTo(taskBack);

        productRequirement.relatedTask(null);
        assertThat(productRequirement.getRelatedTask()).isNull();
    }

    @Test
    void foodItemTest() {
        ProductRequirement productRequirement = getProductRequirementRandomSampleGenerator();
        FoodItem foodItemBack = getFoodItemRandomSampleGenerator();

        productRequirement.setFoodItem(foodItemBack);
        assertThat(productRequirement.getFoodItem()).isEqualTo(foodItemBack);

        productRequirement.foodItem(null);
        assertThat(productRequirement.getFoodItem()).isNull();
    }

    @Test
    void activityTest() {
        ProductRequirement productRequirement = getProductRequirementRandomSampleGenerator();
        Activity activityBack = getActivityRandomSampleGenerator();

        productRequirement.setActivity(activityBack);
        assertThat(productRequirement.getActivity()).isEqualTo(activityBack);

        productRequirement.activity(null);
        assertThat(productRequirement.getActivity()).isNull();
    }
}
