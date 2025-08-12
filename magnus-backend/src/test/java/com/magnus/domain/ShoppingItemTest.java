package com.magnus.domain;

import static com.magnus.domain.AppUserTestSamples.*;
import static com.magnus.domain.ShoppingItemTestSamples.*;
import static com.magnus.domain.WeeklyPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShoppingItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingItem.class);
        ShoppingItem shoppingItem1 = getShoppingItemSample1();
        ShoppingItem shoppingItem2 = new ShoppingItem();
        assertThat(shoppingItem1).isNotEqualTo(shoppingItem2);

        shoppingItem2.setId(shoppingItem1.getId());
        assertThat(shoppingItem1).isEqualTo(shoppingItem2);

        shoppingItem2 = getShoppingItemSample2();
        assertThat(shoppingItem1).isNotEqualTo(shoppingItem2);
    }

    @Test
    void purchasedByTest() {
        ShoppingItem shoppingItem = getShoppingItemRandomSampleGenerator();
        AppUser appUserBack = getAppUserRandomSampleGenerator();

        shoppingItem.setPurchasedBy(appUserBack);
        assertThat(shoppingItem.getPurchasedBy()).isEqualTo(appUserBack);

        shoppingItem.purchasedBy(null);
        assertThat(shoppingItem.getPurchasedBy()).isNull();
    }

    @Test
    void weeklyPlanTest() {
        ShoppingItem shoppingItem = getShoppingItemRandomSampleGenerator();
        WeeklyPlan weeklyPlanBack = getWeeklyPlanRandomSampleGenerator();

        shoppingItem.setWeeklyPlan(weeklyPlanBack);
        assertThat(shoppingItem.getWeeklyPlan()).isEqualTo(weeklyPlanBack);

        shoppingItem.weeklyPlan(null);
        assertThat(shoppingItem.getWeeklyPlan()).isNull();
    }
}
