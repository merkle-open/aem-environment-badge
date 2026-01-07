/**
 * @fileoverview Safely defines and initializes the global Merkle namespace and its
 * Merkle.EnvironmentBadge sub-namespace. This pattern ensures that existing objects
 * are preserved and prevents pollution of the global scope.
 *
 * @dependency {Window} global - The global window object.
 */
((global) => {

    "use strict";

    /**
     * @namespace Merkle
     * @type {Object}
     * @description The root namespace for all components, utilities, and applications
     * developed by Merkle. Acts as the primary container for all custom JS logic.
     * @property {Object} EnvironmentBadge - Sub-namespace for the environment indicator tool.
     * @global
     */
    global.Merkle = global.Merkle || {};

    /**
     * @namespace Merkle.EnvironmentBadge
     * @type {Object}
     * @description The sub-namespace dedicated specifically to the Environment Badge
     * component, housing its core classes, configuration, and state.
     * @memberof Merkle
     */
    global.Merkle.EnvironmentBadge = global.Merkle.EnvironmentBadge || {};

})(window);
