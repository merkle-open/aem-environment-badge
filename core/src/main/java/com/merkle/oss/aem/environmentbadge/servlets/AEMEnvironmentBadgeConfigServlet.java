package com.merkle.oss.aem.environmentbadge.servlets;

import com.google.gson.GsonBuilder;
import com.merkle.oss.aem.environmentbadge.services.AEMEnvironmentBadgeConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPathsStrict;
import org.apache.sling.servlets.post.JSONResponse;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import javax.servlet.Servlet;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Sling Servlet for retrieving the AEM Environment Badge configuration as a JSON object.
 *
 * <p>This servlet is mapped using the {@link SlingServletPathsStrict} annotation to a specific path
 * to serve the necessary configuration data to the frontend JavaScript component via an AJAX request.</p>
 *
 * @see AEMEnvironmentBadgeConfigService
 */
@Component(service = Servlet.class)
@SlingServletPathsStrict(
        extensions = "json",
        methods = HttpConstants.METHOD_GET,
        paths = {
                "/bin/com/merkle/oss/aem/environment-badge/config"
        }
)
public class AEMEnvironmentBadgeConfigServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = -2090658834762276970L;

    private static final String ENABLED_DOCUMENT_TITLE_PREFIX_KEY = "enableDocumentTitlePrefix";
    private static final String DOCUMENT_TITLE_PREFIX_KEY = "documentTitlePrefix";
    private static final String ENABLED_BADGE_KEY = "enableBadge";
    private static final String BADGE_BACKGROUND_COLOR_KEY = "badgeBackgroundColor";
    private static final String BADGE_TITLE_KEY = "badgeTitle";

    /**
     * Reference to the OSGi service that provides the configuration settings for the environment badge.
     * This reference is optional, allowing the servlet to run even if the configuration service is temporarily unavailable.
     * This is mainly to avoid requests errors from the authoring UI towards this servlet on initialization.
     */
    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policyOption = ReferencePolicyOption.GREEDY)
    private transient AEMEnvironmentBadgeConfigService aemEnvironmentBadgeConfigService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(@NotNull final SlingHttpServletRequest request, @NotNull final SlingHttpServletResponse response) throws IOException {
        response.setContentType(JSONResponse.RESPONSE_CONTENT_TYPE);

        final Map<String, Object> configurationDto = new HashMap<>();
        if (!Objects.isNull(aemEnvironmentBadgeConfigService)) {
            configurationDto.put(ENABLED_DOCUMENT_TITLE_PREFIX_KEY, aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix());
            configurationDto.put(DOCUMENT_TITLE_PREFIX_KEY, StringUtils.defaultIfEmpty(aemEnvironmentBadgeConfigService.getDocumentTitlePrefix(), StringUtils.EMPTY));
            configurationDto.put(ENABLED_BADGE_KEY, aemEnvironmentBadgeConfigService.isEnableBadge());
            configurationDto.put(BADGE_BACKGROUND_COLOR_KEY, aemEnvironmentBadgeConfigService.getBadgeBackgroundColor());
            configurationDto.put(BADGE_TITLE_KEY, StringUtils.defaultIfEmpty(aemEnvironmentBadgeConfigService.getBadgeTitle(), StringUtils.EMPTY));
        }

        response.setStatus(HttpStatus.SC_OK);
        // Disable HTML escaping is needed to prevent gson from escaping chars like '=' to '\u003D'
        new GsonBuilder().disableHtmlEscaping().create().toJson(configurationDto, response.getWriter());
    }

}
