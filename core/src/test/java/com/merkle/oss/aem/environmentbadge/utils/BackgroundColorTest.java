package com.merkle.oss.aem.environmentbadge.utils;

import com.merkle.oss.aem.environmentbadge.constants.BackgroundColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BackgroundColorTest {
    /**
     * Test getters and setters.
     *
     * <p>Methods under test:
     *
     * <ul>
     *   <li>{@link BackgroundColor#toString()}
     *   <li>{@link BackgroundColor#getColor()}
     *   <li>{@link BackgroundColor#getColorCode()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        BackgroundColor valueOfResult = BackgroundColor.valueOf("RED");

        String actualToStringResult = valueOfResult.toString();
        String actualColor = valueOfResult.getColor();

        assertEquals("#d7373f", valueOfResult.getColorCode());
        assertEquals("red", actualColor);
        assertEquals("red", actualToStringResult);
    }

    /**
     * Test {@link BackgroundColor#of(String)}.
     *
     * <ul>
     *   <li>When {@code blue}.
     *   <li>Then return {@code BLUE}.
     * </ul>
     *
     * <p>Method under test: {@link BackgroundColor#of(String)}
     */
    @Test
    void testOf_whenBlue_thenReturnBlue() {
        assertEquals(BackgroundColor.BLUE, BackgroundColor.of("blue"));
    }

    /**
     * Test {@link BackgroundColor#of(String)}.
     *
     * <ul>
     *   <li>When {@code Type}.
     *   <li>Then return {@code FUCHSIA}.
     * </ul>
     *
     * <p>Method under test: {@link BackgroundColor#of(String)}
     */
    @Test
    void testOf_whenType_thenReturnFuchsia() {
        assertEquals(BackgroundColor.FUCHSIA, BackgroundColor.of("Type"));
    }

}