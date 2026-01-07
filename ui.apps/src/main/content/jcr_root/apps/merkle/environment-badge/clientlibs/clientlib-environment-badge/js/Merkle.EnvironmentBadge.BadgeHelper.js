/**
 * @fileoverview Defines the core Helper utility object for the Merkle.EnvironmentBadge
 * application, including shared constants, settings, and utility functions.
 *
 * @dependency {Merkle.EnvironmentBadge} namespace - The namespace object.
 */
((namespace) => {

    "use strict";

    /**
     * @typedef {object} BadgeConfig
     * @property {boolean} enableDocumentTitlePrefix - If true, the document title prefix will be enforced.
     * @property {string} documentTitlePrefix - The string to prepend to the document title.
     * @property {boolean} enableBadge - If true, the badge and badge bar should be rendered.
     * @property {string} badgeTitle - The text to display on the badge tag.
     * @property {string} badgeBackgroundColor - The Coral spectrum color string (e.g., 'red', 'blue') for the badge/bar.
     */

    /**
     * @class BadgeHelper
     * @classdesc A static utility class designed to encapsulate application-wide
     * constants, configurable settings, and stateless helper functions.
     * This class should not be instantiated.
     * @memberof Merkle.EnvironmentBadge
     */
    class BadgeHelper {

        /**
         * @constructor
         * @throws {Error} Throws an error if an attempt is made to instantiate this class.
         * @private
         */
        constructor() {
            throw new Error("Cannot instantiate static utility class BadgeHelper.");
        }

        /**
         * Checks if a given value is considered "empty".
         * An object is considered empty if it is null, undefined, or has no
         * enumerable own-properties. Arrays and strings are empty if their length is 0.
         *
         * @param {*} value - The value to check against.
         * @return {boolean} True if the value is empty, otherwise false.
         * @static
         * @public
         */
        static isEmpty(value) {
            if (value == null) {
                return true;
            }

            if (Array.isArray(value) || typeof value === 'string') {
                return value.length === 0;
            }

            return Object.keys(value).length === 0;
        }

        /**
         * Ensures a specific prefix is maintained in the browser document title.
         * This method sets the document title initially and then starts an interval
         * timer to periodically check and re-enforce the title. This is used
         * to prevent other scripts from accidentally or intentionally overwriting a
         * desired, consistent title (e.g., to indicate environment or status).
         * The enforcement mechanism stops itself after the title is successfully
         * verified for a specific number of times.
         *
         * @param {BadgeConfig} config - Configuration object containing badge settings.
         * @return {void}
         * @static
         * @public
         */
        static setDocumentTitlePrefix(config) {
            if (!config.enableDocumentTitlePrefix || this.isEmpty(config.documentTitlePrefix)) {
                return;
            }

            const targetTitle = config.documentTitlePrefix + " | " + document.title;
            document.title = targetTitle;

            let enforcementCount = 0;
            const MAX_CHECKS = this.SETTINGS.DOCUMENT_TITLE_PREFIX_MAX_CHECKS;
            const INTERVAL_DURATION = this.SETTINGS.DOCUMENT_TITLE_PREFIX_CHECK_INTERVAL_DURATION_MS;

            const intervalId = setInterval(() => {
                if (document.title !== targetTitle) {
                    document.title = targetTitle;
                    enforcementCount = 0;
                } else {
                    enforcementCount++;
                    if (enforcementCount > MAX_CHECKS) {
                        clearInterval(intervalId);
                    }
                }
            }, INTERVAL_DURATION);
        }

    }

    /**
     * @static
     * @constant
     * @type {object}
     * @description Constants and key strings used throughout the application for IDs, tags, and keys.
     */
    BadgeHelper.CONST = Object.freeze({
        /** @type {string} The HTML ID attribute for the environment badge element itself. */
        AEM_BADGE_ID: "aem-environment-badge",
        /** @type {string} The HTML ID attribute for the container element holding the environment badge. */
        AEM_BADGE_BAR_ID: "aem-environment-badge-bar",
        /** @type {string} The tag name used for the primary title bar in the AEM UI. */
        BETTY_BAR_TAG: "betty-titlebar-primary",
        /** @type {string} The tag name used for the standard action bar element in the AEM UI. */
        ACTION_BAR_TAG: "coral-actionbar-primary",
        /** @type {string} The CSS class name for the global navigation button. */
        GLOBAL_NAV_BUTTON_CLASS: "globalnav-toggle",
        /** @type {string} The key used to store configuration data in the browser's Session Storage. */
        SESSION_STORAGE_KEY_CONFIG: "com.merkle.oss.aem.environment-badge.config",
        /** @type {string} The URI from which to retrieve the environment badge configurations. */
        CONFIGURATION_SERVLET_URI: "/bin/com/merkle/oss/aem/environment-badge/config.json"
    });

    /**
     * @static
     * @constant
     * @type {object}
     * @description Global settings for the module's behavior, primarily revolving around timing and limits.
     */
    BadgeHelper.SETTINGS = Object.freeze({
        /** @type {number} The maximum number of times the interval check should run to ensure the document title prefix remains set. */
        DOCUMENT_TITLE_PREFIX_MAX_CHECKS: 5,
        /** @type {number} The duration in milliseconds between checks for title prefix enforcement. */
        DOCUMENT_TITLE_PREFIX_CHECK_INTERVAL_DURATION_MS: 1500
    });

    /**
     * @memberof Merkle.EnvironmentBadge
     * @type {typeof BadgeHelper}
     * @description Exposes the static utility class {@link BadgeHelper} containing constants, settings, and helper methods.
     */
    namespace.BadgeHelper = BadgeHelper;

})(window.Merkle.EnvironmentBadge);
