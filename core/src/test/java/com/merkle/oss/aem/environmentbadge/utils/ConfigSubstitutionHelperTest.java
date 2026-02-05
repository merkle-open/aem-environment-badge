package com.merkle.oss.aem.environmentbadge.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    private ConfigSubstitutionHelper helper;

    @BeforeEach
    void setUp() {
        final Map<String, String> substitutionValues = new HashMap<>();
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

    @ParameterizedTest(name = "{index} => source=''{0}'', expected=''{1}''")
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', textBlock = """
            SOURCE                                                              | EXPECTED
            Written by ${author_name}.                                          | Written by JohnDoe.
            ${document_title_prefix} title prefix, ${document_title_prefix} again! | DEV title prefix, DEV again!
            The environment is ${document_title_prefix} with color ${background_color}. | The environment is DEV with color #FF0000.
            This string has no variables to replace.                            | This string has no variables to replace.
            Testing an unknown ${unknown_variable} here.                       | Testing an unknown ${unknown_variable} here.
            ''                                                                  | ''
            """)
    void replace_ShouldHandleVariousSubstitutionScenarios(final String source, final String expected) {
        final String result = helper.replace(source);

        assertEquals(expected, result, () -> "Substitution failed for input: " + source);
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
