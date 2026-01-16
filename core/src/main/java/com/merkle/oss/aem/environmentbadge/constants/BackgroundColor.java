package com.merkle.oss.aem.environmentbadge.constants;

import org.apache.commons.lang3.Strings;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;

/**
 * Represents the allowed background colors for the AEM Environment Badge component.
 * * <p>Each enumeration constant stores a descriptive name (e.g., "red") and its corresponding
 * hex color code (e.g., "#d7373f"). This ensures consistent color usage across the module
 * and allows for easy mapping between configuration strings and actual color values.</p>
 */
public enum BackgroundColor {

    RED("red", "#d7373f"),
    BLUE("blue", "#1473e6"),
    GREEN("green", "#268e6c"),
    ORANGE("orange", "#da7b11"),
    GREY("grey", "#747474"),
    YELLOW("yellow", "#dfbf00"),
    SEAFOAM("seafoam", "#1b959a"),
    FUCHSIA("fuchisa", "#c038cc");

    private final String color;
    private final String colorCode;

    /**
     * Constructs a new {@code BackgroundColor} enumeration constant.
     *
     * @param color     The descriptive name of the color (e.g., "red").
     * @param colorCode The hex code of the color (e.g., "#d7373f").
     */
    BackgroundColor(@NonNull final String color, @NonNull final String colorCode) {
        this.color = color;
        this.colorCode = colorCode;
    }

    /**
     * Gets the descriptive name of the color.
     *
     * @return The color name (e.g., "red").
     */
    public @NonNull String getColor() {
        return color;
    }

    /**
     * Gets the hex code representation of the color.
     *
     * @return The color hex code (e.g., "#d7373f").
     */
    public @NonNull String getColorCode() {
        return colorCode;
    }

    /**
     * {@inheritDoc}
     *
     * @see Enum#toString()
     */
    @Override
    public @NonNull String toString() {
        return getColor();
    }

    /**
     * Retrieves the {@code BackgroundColor} enum constant corresponding to the given string type (color name).
     *
     * <p>The comparison is case-sensitive using {@code Strings.CS.equals}.</p>
     *
     * @param type The descriptive color name (e.g., "blue") to search for.
     * @return The matching {@code BackgroundColor} constant. Returns {@link #FUCHSIA} if no match is found.
     */
    public static @NonNull BackgroundColor of(@Nullable final String type) {
        return Arrays.stream(BackgroundColor.values())
                .filter(color -> Strings.CS.equals(color.getColor(), type))
                .findFirst()
                .orElse(FUCHSIA);
    }

}
