package com.magnus.domain;

import static com.magnus.domain.FoodItemTestSamples.*;
import static com.magnus.domain.MenuItemTestSamples.*;
import static com.magnus.domain.MenuTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Menu.class);
        Menu menu1 = getMenuSample1();
        Menu menu2 = new Menu();
        assertThat(menu1).isNotEqualTo(menu2);

        menu2.setId(menu1.getId());
        assertThat(menu1).isEqualTo(menu2);

        menu2 = getMenuSample2();
        assertThat(menu1).isNotEqualTo(menu2);
    }

    @Test
    void menuItemTest() {
        Menu menu = getMenuRandomSampleGenerator();
        MenuItem menuItemBack = getMenuItemRandomSampleGenerator();

        menu.addMenuItem(menuItemBack);
        assertThat(menu.getMenuItems()).containsOnly(menuItemBack);
        assertThat(menuItemBack.getMenu()).isEqualTo(menu);

        menu.removeMenuItem(menuItemBack);
        assertThat(menu.getMenuItems()).doesNotContain(menuItemBack);
        assertThat(menuItemBack.getMenu()).isNull();

        menu.menuItems(new HashSet<>(Set.of(menuItemBack)));
        assertThat(menu.getMenuItems()).containsOnly(menuItemBack);
        assertThat(menuItemBack.getMenu()).isEqualTo(menu);

        menu.setMenuItems(new HashSet<>());
        assertThat(menu.getMenuItems()).doesNotContain(menuItemBack);
        assertThat(menuItemBack.getMenu()).isNull();
    }

    @Test
    void includedFoodItemsTest() {
        Menu menu = getMenuRandomSampleGenerator();
        FoodItem foodItemBack = getFoodItemRandomSampleGenerator();

        menu.addIncludedFoodItems(foodItemBack);
        assertThat(menu.getIncludedFoodItems()).containsOnly(foodItemBack);

        menu.removeIncludedFoodItems(foodItemBack);
        assertThat(menu.getIncludedFoodItems()).doesNotContain(foodItemBack);

        menu.includedFoodItems(new HashSet<>(Set.of(foodItemBack)));
        assertThat(menu.getIncludedFoodItems()).containsOnly(foodItemBack);

        menu.setIncludedFoodItems(new HashSet<>());
        assertThat(menu.getIncludedFoodItems()).doesNotContain(foodItemBack);
    }
}
