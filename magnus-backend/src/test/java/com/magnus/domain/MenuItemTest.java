package com.magnus.domain;

import static com.magnus.domain.MenuItemTestSamples.*;
import static com.magnus.domain.MenuTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.magnus.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MenuItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MenuItem.class);
        MenuItem menuItem1 = getMenuItemSample1();
        MenuItem menuItem2 = new MenuItem();
        assertThat(menuItem1).isNotEqualTo(menuItem2);

        menuItem2.setId(menuItem1.getId());
        assertThat(menuItem1).isEqualTo(menuItem2);

        menuItem2 = getMenuItemSample2();
        assertThat(menuItem1).isNotEqualTo(menuItem2);
    }

    @Test
    void menuTest() {
        MenuItem menuItem = getMenuItemRandomSampleGenerator();
        Menu menuBack = getMenuRandomSampleGenerator();

        menuItem.setMenu(menuBack);
        assertThat(menuItem.getMenu()).isEqualTo(menuBack);

        menuItem.menu(null);
        assertThat(menuItem.getMenu()).isNull();
    }
}
