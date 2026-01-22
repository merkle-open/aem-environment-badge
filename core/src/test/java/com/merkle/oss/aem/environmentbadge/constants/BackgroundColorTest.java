package com.merkle.oss.aem.environmentbadge.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link BackgroundColor} class.
 */
class BackgroundColorTest {

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link BackgroundColor#toString()}
     *   <li>{@link BackgroundColor#getColor()}
     *   <li>{@link BackgroundColor#getColorCode()}
     * </ul>
     */
    @Test
    void testGettersAndSetters() {
        final BackgroundColor valueOfResult = BackgroundColor.valueOf("RED");
        final String actualToStringResult = valueOfResult.toString();
        final String actualColor = valueOfResult.getColor();

        assertEquals("#d7373f", valueOfResult.getColorCode());
        assertEquals("red", actualColor);
        assertEquals("red", actualToStringResult);
    }

    /**
     * Method under test: {@link BackgroundColor#of(String)}
     */
    @Test
    void testOf_whenBlue_thenReturnBlue() {
        assertEquals(BackgroundColor.BLUE, BackgroundColor.of("blue"));
    }

    /**
     * Method under test: {@link BackgroundColor#of(String)}
     */
    @Test
    void testOf_whenType_thenReturnFuchsia() {
        assertEquals(BackgroundColor.FUCHSIA, BackgroundColor.of("Type"));
    }

}
