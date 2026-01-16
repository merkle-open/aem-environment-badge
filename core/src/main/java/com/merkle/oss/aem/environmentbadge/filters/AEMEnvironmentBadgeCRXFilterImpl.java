package com.merkle.oss.aem.environmentbadge.filters;

import com.merkle.oss.aem.environmentbadge.constants.BackgroundColor;
import com.merkle.oss.aem.environmentbadge.models.CharResponseWrapper;
import com.merkle.oss.aem.environmentbadge.services.AEMEnvironmentBadgeConfigService;
import com.merkle.oss.aem.environmentbadge.utils.ConfigSubstitutionHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.jspecify.annotations.NonNull;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * A servlet filter that captures the outgoing response content, allows it to be
 * modified, and then writes the modified content back to the client.
 * <p>
 * This filter wraps the {@link HttpServletResponse} using a
 * {@link CharResponseWrapper} so that all text written by downstream servlets
 * or JSPs is captured rather than being sent immediately to the client. After
 * the filter chain completes, the captured output can be inspected, transformed,
 * or appended to before being written to the actual response stream.
 * </p>
 * This filter is limited to requests matching the index.jsp of the AEM package manager
 * defined by {@code AEMEnvironmentBadgeCRXFilterImpl.ACCEPTED_PATHS}
 *
 * <h3>Use Case</h3>
 * <ul>
 *   <li>Appending environment required HTML to the rendered JSP</li>
 *   <li>Large responses will be stored fully in memory while this filter runs.
 *   Therefore, displaying the environment badge for sling scope based authoring pages
 *   is handled via a client library
 *   </li>
 * </ul>
 *
 */
@Component(
        service = Filter.class,
        property = {
                HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_REGEX + "=" + ".*index.jsp",
                HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT + "=" + "(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "=*)"
        }
)
public class AEMEnvironmentBadgeCRXFilterImpl implements Filter {

    private static final List<String> ACCEPTED_PATHS = Arrays.asList("/crx/de/index.jsp", "/crx/packmgr/index.jsp");

    private static final String BAR_DIV_ID = "aem-environment-badge-bar";

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private AEMEnvironmentBadgeConfigService aemEnvironmentBadgeConfigService;

    @Override
    public void doFilter(@NonNull final ServletRequest request, @NonNull final ServletResponse response, @NonNull final FilterChain chain) throws IOException, ServletException {
        Objects.requireNonNull(request);
        Objects.requireNonNull(response);
        Objects.requireNonNull(chain);

        // Abort further processing if settings are not enabled
        if (!aemEnvironmentBadgeConfigService.isEnableBadge() && !aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix()) {
            chain.doFilter(request, response);
            return;
        }

        final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // Abort further processing for unaccepted paths
        if (!accepts(httpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        // Wrap the original response to capture output
        final CharResponseWrapper wrappedResponse = new CharResponseWrapper(httpServletResponse);

        // Pass request and wrapped response through the filter chain
        chain.doFilter(request, wrappedResponse);

        // Retrieve the captured output produced by downstream of the CRX JSP
        final String originalContent = wrappedResponse.getCapturedOutput();

        // Appending Environment Badge required HTML
        final ConfigSubstitutionHelper configSubstitutionHelper = ConfigSubstitutionHelper.create(createSubstitutionValues());
        final String modifiedContent = createModifiedContent(originalContent, configSubstitutionHelper);

        // Write final content to the real response
        response.setContentLength(modifiedContent.getBytes(response.getCharacterEncoding()).length);
        response.getWriter().write(modifiedContent);
    }

    private boolean accepts(@NonNull final HttpServletRequest httpServletRequest) {
        return ACCEPTED_PATHS.contains(httpServletRequest.getRequestURI());
    }

    private @NonNull Map<String, String> createSubstitutionValues() {
        final Map<String, String> substitutionValues = new HashMap<>();
        substitutionValues.put(ConfigSubstitutionHelper.PLACEHOLDER_DOCUMENT_TITLE_PREFIX, aemEnvironmentBadgeConfigService.getDocumentTitlePrefix());
        final String backgroundColorCode = BackgroundColor.of(aemEnvironmentBadgeConfigService.getBadgeBackgroundColor()).getColorCode();
        substitutionValues.put(ConfigSubstitutionHelper.PLACEHOLDER_BACKGROUND_COLOR, backgroundColorCode);
        return substitutionValues;
    }

    private @NonNull String createModifiedContent(@NonNull final String originalContent, @NonNull final ConfigSubstitutionHelper substitutionHelper) {
        final StringBuilder stringBuilder = new StringBuilder(StringUtils.substringBeforeLast(originalContent, "</body></html>"))
                .append("\n<!-- AEM Environment Badge - Start -->")
                .append("\n");

        if (aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix()) {
            stringBuilder.append(substitutionHelper.replace(createDocumentTitlePrefixScript()))
                    .append("\n");
        }

        if (aemEnvironmentBadgeConfigService.isEnableBadge()) {
            stringBuilder.append("<div id=" + BAR_DIV_ID + "></div>")
                    .append("\n")
                    .append(substitutionHelper.replace(createCSSStyleScript()))
                    .append("\n");
        }

        stringBuilder.append("<!-- AEM Environment Badge - End -->")
                .append("\n</body></html>");

        return stringBuilder.toString();
    }

    private @NonNull String createDocumentTitlePrefixScript() {
        return new StringBuilder("<script>(function(){const t='")
                .append(StringSubstitutor.DEFAULT_VAR_START)
                .append(ConfigSubstitutionHelper.PLACEHOLDER_DOCUMENT_TITLE_PREFIX)
                .append(StringSubstitutor.DEFAULT_VAR_END)
                .append(" | '+document.title;document.title=t;let e=0;const n=5,c=1500;const i=setInterval(()=>{if(document.title!==t){document.title=t,e=0}else if(++e>n)clearInterval(i)},c)})();</script>")
                .toString();
    }

    private @NonNull String createCSSStyleScript() {
        return new StringBuilder("<style>#")
                .append(BAR_DIV_ID)
                .append("{")
                .append("background-color:")
                .append(StringSubstitutor.DEFAULT_VAR_START)
                .append(ConfigSubstitutionHelper.PLACEHOLDER_BACKGROUND_COLOR)
                .append(StringSubstitutor.DEFAULT_VAR_END)
                .append(";")
                .append("position:fixed;")
                .append("left:0;")
                .append("top:0;")
                .append("right:0;")
                .append("height:5px;")
                .append("z-index:100000000000000")
                .append("}")
                .append("</style>")
                .toString();
    }

    /**
     * {@inheritDoc}
     *
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(final FilterConfig filterConfig) {
        // No initialization required
    }

    /**
     * {@inheritDoc}
     *
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {
        // Nothing to clean up
    }

}
