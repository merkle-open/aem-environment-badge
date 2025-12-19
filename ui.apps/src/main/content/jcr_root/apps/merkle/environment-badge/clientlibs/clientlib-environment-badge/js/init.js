/**
 * @fileoverview Application entry point and initialization script for the AEM Environment Badge component.
 * This script handles asynchronous configuration retrieval (checking session storage first),
 * initializes the core Badge functionality, and sets up DOM event listeners.
 *
 * @dependency {jQuery} $ - The jQuery library.
 * @dependency {object} namespace - The {@link Merkle.EnvironmentBadge} namespace object.
 */
(function ($, namespace) {

    "use strict";


    /**
     * @type {object}
     * @description Local reference to the static utility helper class ({@link Merkle.EnvironmentBadge.BadgeHelper}).
     * @const
     */
    const BadgeHelper = namespace.BadgeHelper;

    /**
     * @type {object}
     * @description Local reference to the core Badge class ({@link Merkle.EnvironmentBadge.Badge}).
     * @const
     */
    const Badge = namespace.Badge;

    /**
     * Retrieves the configuration object for the environment badge.
     *
     * The function prioritizes reading from Session Storage. If no configuration is found,
     * it falls back to an asynchronous AJAX request to the configuration servlet.
     * If the servlet returns null or an empty configuration, the status is stored.
     *
     * @function getConfig
     * @returns {jQuery.Promise<object>} A Promise that resolves with the configuration object.
     * Returns an empty object (`{}`) if the configuration is explicitly deactivated,
     * fails to load via AJAX, or cannot be retrieved.
     */
    function getConfig() {
        const KEY = BadgeHelper.CONST.SESSION_STORAGE_KEY_CONFIG;
        let config = sessionStorage.getItem(KEY);

        if (config === '{}') {
            return $.Deferred().resolve({}).promise();
        }

        if (BadgeHelper.isEmpty(config)) {
            return $.getJSON(BadgeHelper.CONST.CONFIGURATION_SERVLET_URI)
                .then(function (data) {
                    if (data === null || (Array.isArray(data) && data.length === 0)) {
                        sessionStorage.setItem(KEY, '{}');
                        return {};
                    }

                    sessionStorage.setItem(KEY, JSON.stringify(data));
                    return data;
                })
                .fail(function (error) {
                    console.error("Failed to load environment badge settings via AJAX:", error);
                    return {};
                });
        }

        return $.Deferred().resolve(JSON.parse(config)).promise();
    }

    /**
     * Initializes the Environment Badge application flow.
     *
     * This function retrieves the necessary configuration, enforces the
     * document title prefix, and instantiates the {@link Merkle.EnvironmentBadge.Badge} class if enabled.
     * It also sets up a click listener on the global navigation button to
     * re-initialize the badge after certain UI events (e.g., side panel opening).
     *
     * @function init
     * @returns {jQuery.Promise<void>} A Promise that resolves when initialization logic is finished.
     */
    function init() {
        return getConfig().then(function (config) {
            BadgeHelper.setDocumentTitlePrefix(config);

            if (!config.enableBadge) {
                return;
            }

            new Badge(config);

            const globalNavButton = document.querySelector('.' + BadgeHelper.CONST.GLOBAL_NAV_BUTTON_CLASS);
            if (globalNavButton) {
                globalNavButton.addEventListener('click', function () {
                    // Use setTimeout to wait for the AEM UI transition to complete before re-injecting the badge.
                    setTimeout(function () {
                        new Badge(config);
                    }, 500);
                });
            }
        });
    }

    /**
     * Main application entry point.
     * Executes the initialization function once the DOM is fully loaded.
     */
    document.addEventListener('DOMContentLoaded', function () {
        init();
    });

}(window.jQuery, window.Merkle.EnvironmentBadge));
