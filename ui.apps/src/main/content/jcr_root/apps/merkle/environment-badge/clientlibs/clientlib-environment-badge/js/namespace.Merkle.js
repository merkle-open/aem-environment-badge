/**
 * @fileoverview Safely defines and initializes the global Merkle namespace and its
 * Merkle.EnvironmentBadge sub-namespace. This pattern ensures that existing objects
 * are preserved and prevents pollution of the global scope.
 *
 * @dependency {Window} global - The global object, typically 'window'.
 */
((global) => {

    "use strict";

    /**
     * @namespace Merkle
     * @description The root namespace for all components, utilities, and applications
     * developed by Merkle. This global object acts as a container to prevent
     * polluting the global scope (`window`).
     * @global
     */
    global.Merkle = global.Merkle || {};

    /**
     * @namespace Merkle.EnvironmentBadge
     * @description The sub-namespace dedicated specifically to the Environment Badge
     * component, housing its core classes, configuration, and state.
     * @memberof Merkle
     */
    global.Merkle.EnvironmentBadge = global.Merkle.EnvironmentBadge || {};

})(window);
