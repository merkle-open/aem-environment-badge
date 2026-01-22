package com.merkle.oss.aem.environmentbadge.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.merkle.oss.aem.environmentbadge.utils.ConfigSubstitutionHelper.PLACEHOLDER_BACKGROUND_COLOR;
import static com.merkle.oss.aem.environmentbadge.utils.ConfigSubstitutionHelper.PLACEHOLDER_DOCUMENT_TITLE_PREFIX;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ConfigSubstitutionHelper} class.
 */
class ConfigSubstitutionHelperTest {

    private Map<String, String> substitutionValues;
    private ConfigSubstitutionHelper helper;

    @BeforeEach
    void setUp() {
        substitutionValues = new HashMap<>();
        substitutionValues.put(PLACEHOLDER_DOCUMENT_TITLE_PREFIX, "DEV");
        substitutionValues.put(PLACEHOLDER_BACKGROUND_COLOR, "#FF0000");
        substitutionValues.put("author_name", "JohnDoe");

        helper = ConfigSubstitutionHelper.create(substitutionValues);
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#create(Map)}
     */
    @Test
    void create_ShouldReturnNewInstance() {
        assertThrows(NullPointerException.class, () -> ConfigSubstitutionHelper.create(null));
        assertNotNull(helper, "The factory method should return a non-null instance.");
        assertInstanceOf(ConfigSubstitutionHelper.class, helper, "The returned object must be of the correct type.");
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#replace(String)}
     */
    @Test
    void replace_ShouldReplaceKnownPlaceholders() {
        final String source = "The environment is ${document_title_prefix} with color ${background_color}.";
        final String expected = "The environment is DEV with color #FF0000.";

        final String result = helper.replace(source);

        assertEquals(expected, result, "Should replace both predefined constants correctly.");
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#replace(String)}
     */
    @Test
    void replace_ShouldReplaceCustomPlaceholder() {
        final String source = "Written by ${author_name}.";
        final String expected = "Written by JohnDoe.";

        final String result = helper.replace(source);

        assertEquals(expected, result, "Should replace custom placeholders correctly.");
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#replace(String)}
     */
    @Test
    void replace_ShouldHandleMultipleOccurrencesOfTheSamePlaceholder() {
        final String source = "${document_title_prefix} title prefix, ${document_title_prefix} again!";
        final String expected = "DEV title prefix, DEV again!";

        final String result = helper.replace(source);

        assertEquals(expected, result, "Should replace the same placeholder multiple times.");
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#replace(String)}
     */
    @Test
    void replace_ShouldHandleSourceStringWithNoPlaceholders() {
        final String source = "This string has no variables to replace.";

        final String result = helper.replace(source);

        assertEquals(source, result, "The string should remain unmodified.");
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#replace(String)}
     */
    @Test
    void replace_ShouldLeaveUnknownPlaceholderUnmodified() {
        final String source = "Testing an unknown ${unknown_variable} here.";
        final String expected = "Testing an unknown ${unknown_variable} here.";

        final String result = helper.replace(source);

        assertEquals(expected, result, "Unknown placeholders should be left as is.");
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#replace(String)}
     */
    @Test
    void replace_ShouldHandleEmptySourceString() {
        final String source = "";
        final String expected = "";

        final String result = helper.replace(source);

        assertEquals(expected, result, "An empty source string should return an empty string.");
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#replace(String)}
     */
    @Test
    void replace_ShouldHandleNullSourceString() {
        final String result = helper.replace(null);

        assertNull(result, "A null source string should return null.");
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#create(Map)}
     */
    @Test
    void create_ShouldHandleEmptySubstitutionMap() {
        final ConfigSubstitutionHelper emptyHelper = ConfigSubstitutionHelper.create(Collections.emptyMap());
        final String source = "Test with ${background_color}.";

        final String result = emptyHelper.replace(source);

        assertEquals(source, result, "If the map is empty, placeholders should remain.");
    }

    /**
     * Method under test: {@link ConfigSubstitutionHelper#create(Map)}
     */
    @Test
    void create_ShouldHandleMapImmutability() {
        final Map<String, String> initialMap = new HashMap<>();
        initialMap.put("key", "initial");

        final ConfigSubstitutionHelper consistentHelper = ConfigSubstitutionHelper.create(initialMap);

        initialMap.put("key", "changed");
        initialMap.put("new_key", "new_value");

        final String source = "Value is ${key}.";
        final String expected = "Value is initial.";

        final String result = consistentHelper.replace(source);

        assertEquals(expected, result, "The helper should use a copy of the map and ignore external changes.");
    }

}
