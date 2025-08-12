package com.magnus.domain;

import static com.magnus.domain.FoodItemTestSamples.*;
import static com.magnus.domain.MenuTestSamples.*;
import static com.magnus.domain.ProductRequirementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FoodItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FoodItem.class);
        FoodItem foodItem1 = getFoodItemSample1();
        FoodItem foodItem2 = new FoodItem();
        assertThat(foodItem1).isNotEqualTo(foodItem2);

        foodItem2.setId(foodItem1.getId());
        assertThat(foodItem1).isEqualTo(foodItem2);

        foodItem2 = getFoodItemSample2();
        assertThat(foodItem1).isNotEqualTo(foodItem2);
    }

    @Test
    void productRequirementTest() {
        FoodItem foodItem = getFoodItemRandomSampleGenerator();
        ProductRequirement productRequirementBack = getProductRequirementRandomSampleGenerator();

        foodItem.addProductRequirement(productRequirementBack);
        assertThat(foodItem.getProductRequirements()).containsOnly(productRequirementBack);
        assertThat(productRequirementBack.getFoodItem()).isEqualTo(foodItem);

        foodItem.removeProductRequirement(productRequirementBack);
        assertThat(foodItem.getProductRequirements()).doesNotContain(productRequirementBack);
        assertThat(productRequirementBack.getFoodItem()).isNull();

        foodItem.productRequirements(new HashSet<>(Set.of(productRequirementBack)));
        assertThat(foodItem.getProductRequirements()).containsOnly(productRequirementBack);
        assertThat(productRequirementBack.getFoodItem()).isEqualTo(foodItem);

        foodItem.setProductRequirements(new HashSet<>());
        assertThat(foodItem.getProductRequirements()).doesNotContain(productRequirementBack);
        assertThat(productRequirementBack.getFoodItem()).isNull();
    }

    @Test
    void availableMenusTest() {
        FoodItem foodItem = getFoodItemRandomSampleGenerator();
        Menu menuBack = getMenuRandomSampleGenerator();

        foodItem.addAvailableMenus(menuBack);
        assertThat(foodItem.getAvailableMenus()).containsOnly(menuBack);
        assertThat(menuBack.getIncludedFoodItems()).containsOnly(foodItem);

        foodItem.removeAvailableMenus(menuBack);
        assertThat(foodItem.getAvailableMenus()).doesNotContain(menuBack);
        assertThat(menuBack.getIncludedFoodItems()).doesNotContain(foodItem);

        foodItem.availableMenus(new HashSet<>(Set.of(menuBack)));
        assertThat(foodItem.getAvailableMenus()).containsOnly(menuBack);
        assertThat(menuBack.getIncludedFoodItems()).containsOnly(foodItem);

        foodItem.setAvailableMenus(new HashSet<>());
        assertThat(foodItem.getAvailableMenus()).doesNotContain(menuBack);
        assertThat(menuBack.getIncludedFoodItems()).doesNotContain(foodItem);
    }
}
