/**
 * @fileoverview Defines the core Badge class and its methods for rendering and
 * managing the AEM Environment Badge components within the DOM.
 *
 * @dependency {jQuery} $ - The jQuery library.
 * @dependency {Merkle.EnvironmentBadge} namespace - The namespace object.
 */
(function ($, namespace) {

    "use strict";

    /**
     * @type {typeof Merkle.EnvironmentBadge.BadgeHelper}
     * @description Local reference to the static utility helper class ({@link Merkle.EnvironmentBadge.BadgeHelper}).
     * @const
     */
    const BadgeHelper = namespace.BadgeHelper;

    /**
     * @class Badge
     * @classdesc Core class responsible for constructing and injecting the environment badge and
     * associated bar into the AEM UI.
     * @memberof Merkle.EnvironmentBadge
     */
    class Badge {

        /**
         * Initializes the Badge component.
         * The badge and badge bar are built and inserted into the DOM only if
         * the {@link BadgeConfig} enables it.
         *
         * @constructor
         * @param {BadgeConfig} config - Configuration object containing badge settings.
         */
        constructor(config) {
            if (config.enableBadge) {
                this._buildBadge(config);
                this._buildBadgeBar(config);
            }
        }

        /**
         * Constructs the HTML for the badge and attempts to inject it into the appropriate AEM UI container.
         * Locates either the Betty Title bar or the Coral Actionbar based on presence.
         *
         * @private
         * @param {BadgeConfig} config - Configuration object containing badge settings.
         * @returns {void}
         */
        _buildBadge(config) {
            if (BadgeHelper.isEmpty(config.badgeTitle)) {
                return;
            }

            const BETTY_TAG = BadgeHelper.CONST.BETTY_BAR_TAG;
            const ACTION_TAG = BadgeHelper.CONST.ACTION_BAR_TAG;
            const bettyBars = document.querySelectorAll(BETTY_TAG);
            const actionBar = document.querySelector(ACTION_TAG);

            const badge = `
                <coral-tag id="${BadgeHelper.CONST.AEM_BADGE_ID}"  
                        color="${config.badgeBackgroundColor}"
                        class="_coral-Label _coral-Label--small _coral-Label--${config.badgeBackgroundColor}" 
                        size="S">
                    <coral-tag-label class="_coral-Tags-itemLabel">${config.badgeTitle}</coral-tag-label>
                </coral-tag>
                `;

            if (bettyBars.length > 0) {
                bettyBars.forEach(target => {
                    this._appendBadgeToTarget(target, badge);
                });
            } else if (actionBar) {
                this._appendBadgeToTarget(actionBar, badge);
            }
        }

        /**
         * Safely appends the badge HTML to a target element, ensuring the badge is not duplicated.
         *
         * @private
         * @param {HTMLElement} target - The DOM element to append the badge to (e.g., Betty bar or Action bar).
         * @param {string} badge - The HTML string representation of the badge element.
         * @returns {void}
         */
        _appendBadgeToTarget(target, badge) {
            if (!target || target.querySelector(`#${BadgeHelper.CONST.AEM_BADGE_ID}`) !== null) {
                return;
            }

            if (target.lastElementChild !== null) {
                // Insert after the last child (e.g., after the last button/action)
                target.lastElementChild.insertAdjacentHTML("afterend", badge);
            } else {
                // Insert as the first element if the container is empty
                target.insertAdjacentHTML("afterbegin", badge);
            }
        }

        /**
         * Constructs and prepends the document-spanning badge bar element to the document body.
         * The bar is used for visual environmental highlighting at the very top of the viewport.
         *
         * @private
         * @param {BadgeConfig} config - Configuration object containing badge settings.
         * @returns {void}
         */
        _buildBadgeBar(config) {
            const bar = `
                <div id="${BadgeHelper.CONST.AEM_BADGE_BAR_ID}"
                        class="_coral-Label--${config.badgeBackgroundColor}"></div>
                `;

            // Insert the bar right at the start of the <body> element.
            document.body.insertAdjacentHTML("afterbegin", bar);
        }
    }

    /**
     * @memberof Merkle.EnvironmentBadge
     * @type {typeof Badge}
     * @description Exposes the core {@link Badge} class under the 'Badge' property of the namespace.
     */
    namespace.Badge = Badge;

}(window.jQuery, window.Merkle.EnvironmentBadge));
