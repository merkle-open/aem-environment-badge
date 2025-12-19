package com.merkle.oss.aem.environmentbadge.services;

/**
 * Service interface for retrieving configuration settings related to the AEM Environment Badge.
 *
 * <p>Implementations of this service source configuration values from OSGi configurations,
 * providing a consistent layer for other components (like servlets or models) to access
 * badge behavior and appearance settings.</p>
 */
public interface AEMEnvironmentBadgeConfigService {

    /**
     * Checks if the enforcement of a document title prefix is enabled and set.
     *
     * @return {@code true} if the prefix should be added to the browser tab title; otherwise {@code false}.
     */
    boolean isEnableDocumentTitlePrefix();

    /**
     * Retrieves the string that should be prepended to the browser tab title.
     *
     * @return The configured document title prefix (e.g., {@code <PREFIX> | <BROWSER TITLE>}).
     */
    String getDocumentTitlePrefix();

    /**
     * Checks if the visual environment badge component is enabled for rendering in the AEM UI.
     *
     * @return {@code true} if the badge should be built and displayed; otherwise {@code false}.
     */
    boolean isEnableBadge();

    /**
     * Retrieves the text to be displayed on the environment badge component.
     *
     * @return The configured badge title text.
     */
    String getBadgeTitle();

    /**
     * Retrieves the color string used to style the environment badge and its associated bar.
     *
     * @return The configured background color string.
     */
    String getBadgeBackgroundColor();

}
