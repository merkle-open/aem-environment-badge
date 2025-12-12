package com.merkle.oss.aem.environmentbadge.utils;

import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class for performing variable substitution with configuration strings.
 * <p>
 * This class uses an underlying mechanism of {@link org.apache.commons.text.StringSubstitutor} to replace
 * placeholders (e.g., {@code ${placeholder_key_value}}) in a source string with values
 * provided during the helper's creation.
 * </p>
 */
public class ConfigSubstitutionHelper {

    /**
     * Constant for the configuration placeholder key representing a document title prefix.
     */
    public static final String PLACEHOLDER_DOCUMENT_TITLE_PREFIX = "document_title_prefix";

    /**
     * Constant for the configuration placeholder key representing a background color value.
     */
    public static final String PLACEHOLDER_BACKGROUND_COLOR = "background_color";

    private final StringSubstitutor substitutor;

    /**
     * Constructs a new {@code ConfigSubstitutionHelper} instance, initializing the underlying
     * substitution mechanism with the provided map of values.
     * <p>
     * The map is copied internally to ensure the helper's substitution values
     * remain consistent even if the source map is later modified.
     * </p>
     *
     * @param substitutionValues The key-value map where keys are the placeholder names
     *                           (without prefix/suffix, e.g., "variable_name") and
     *                           values are the replacement strings.
     */
    private ConfigSubstitutionHelper(final Map<String, String> substitutionValues) {
        this.substitutor = new StringSubstitutor(new HashMap<>(substitutionValues));
    }

    /**
     * Factory method to create a new instance of {@code ConfigSubstitutionHelper}.
     *
     * @param substitutionValues The map containing key-value pairs for substitution.
     *                           Keys correspond to placeholder names, and values are the
     *                           strings to substitute in.
     * @return A new, initialized {@code ConfigSubstitutionHelper} instance.
     */
    public static ConfigSubstitutionHelper create(final Map<String, String> substitutionValues) {
        return new ConfigSubstitutionHelper(substitutionValues);
    }

    /**
     * Replaces all defined placeholders in the given source string with their
     * corresponding substitution values.
     *
     * @param source The string containing placeholders (e.g., "${placeholder_key_value}").
     * @return The source string with all placeholders replaced by their configured values.
     * If no substitution values are found for a placeholder, it may be left
     * unmodified depending on the underlying substitution mechanism's configuration.
     */
    public String replace(final String source) {
        return substitutor.replace(source);
    }

}