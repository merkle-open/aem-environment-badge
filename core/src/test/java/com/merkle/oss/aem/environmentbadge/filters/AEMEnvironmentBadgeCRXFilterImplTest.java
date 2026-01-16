package com.merkle.oss.aem.environmentbadge.filters;

import com.merkle.oss.aem.environmentbadge.constants.BackgroundColor;
import com.merkle.oss.aem.environmentbadge.models.CharResponseWrapper;
import com.merkle.oss.aem.environmentbadge.services.AEMEnvironmentBadgeConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link AEMEnvironmentBadgeCRXFilterImpl} class.
 */
@ExtendWith(MockitoExtension.class)
class AEMEnvironmentBadgeCRXFilterImplTest {

    @Mock
    private AEMEnvironmentBadgeConfigService aemEnvironmentBadgeConfigService;

    @InjectMocks
    private AEMEnvironmentBadgeCRXFilterImpl filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;


    private final StringWriter responseWriter = new StringWriter();
    private final PrintWriter printWriter = new PrintWriter(responseWriter);

    private final String ORIGINAL_RESPONSE = "<html><head><title>Original Title</title></head><body>Content</body></html>";
    private final String CRX_DE_PATH = "/crx/de/index.jsp";

    /**
     * <p>Method under test: {@link AEMEnvironmentBadgeCRXFilterImpl#doFilter(ServletRequest, ServletResponse, FilterChain)}
     */
    @Test
    void doFilter_NotEnabled_null() {
        assertThrows(NullPointerException.class, () -> filter.doFilter(null, response, chain));
        assertThrows(NullPointerException.class, () -> filter.doFilter(request, null, chain));
        assertThrows(NullPointerException.class, () -> filter.doFilter(request, response, null));
    }

    /**
     * <p>Method under test: {@link AEMEnvironmentBadgeCRXFilterImpl#doFilter(ServletRequest, ServletResponse, FilterChain)}
     */
    @Test
    void doFilter_NotEnabled_ShouldAbortAndPassThrough() throws IOException, ServletException {
        when(aemEnvironmentBadgeConfigService.isEnableBadge()).thenReturn(false);
        when(aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix()).thenReturn(false);

        filter.doFilter(request, response, chain);

        // Execution stops and passes through the chain with the ORIGINAL response.
        verify(chain, times(1)).doFilter(request, response);
        // Assert that a response writer was never initialized/called, proving early exit.
        verify(response, never()).getWriter();
    }

    /**
     * <p>Method under test: {@link AEMEnvironmentBadgeCRXFilterImpl#doFilter(ServletRequest, ServletResponse, FilterChain)}
     */
    @Test
    void doFilter_PathNotAccepted_ShouldAbortAndPassThrough() throws IOException, ServletException {
        when(aemEnvironmentBadgeConfigService.isEnableBadge()).thenReturn(true);
        when(request.getRequestURI()).thenReturn("/editor.html/content/site/en.html");

        filter.doFilter(request, response, chain);

        // Execution stops and passes through the chain with the ORIGINAL response.
        verify(chain, times(1)).doFilter(request, response);
        // Assert that a response writer was never initialized/called, proving early exit.
        verify(response, never()).getWriter();
    }

    /**
     * <p>Method under test: {@link AEMEnvironmentBadgeCRXFilterImpl#doFilter(ServletRequest, ServletResponse, FilterChain)}
     */
    @Test
    void doFilter_AcceptedPathAndBadgeEnabled_ShouldModifyAndWriteContent() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(response.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());

        when(aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix()).thenReturn(false);
        when(aemEnvironmentBadgeConfigService.isEnableBadge()).thenReturn(true);
        when(aemEnvironmentBadgeConfigService.getBadgeBackgroundColor()).thenReturn(BackgroundColor.FUCHSIA.getColor());

        when(request.getRequestURI()).thenReturn(CRX_DE_PATH);

        // When the chain.doFilter is called with the wrapped response,
        // we capture that wrapper instance and write the original content to it.
        doAnswer(invocation -> {
            final CharResponseWrapper wrapper = invocation.getArgument(1);
            wrapper.getWriter().write(ORIGINAL_RESPONSE);
            return null;
        }).when(chain).doFilter(eq(request), any(CharResponseWrapper.class));

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(eq(request), any(CharResponseWrapper.class));
        verify(response, times(1)).getWriter();
        verify(response, times(1)).setContentLength(anyInt());

        String finalContent = responseWriter.toString();

        final java.lang.reflect.Field barDivIdField = AEMEnvironmentBadgeCRXFilterImpl.class.getDeclaredField("BAR_DIV_ID");
        barDivIdField.setAccessible(true);
        final String barDivIdValue = (String) barDivIdField.get(null);

        assert (finalContent.contains("<div id=" + barDivIdValue + "></div>"));
        assert (!finalContent.contains("<script>(function(){const t='DEV | '+document.title;document.title=t;let e=0;const n=5,c=1500;"));
        assert (finalContent.contains("<style>#" + barDivIdValue + "{background-color:" + BackgroundColor.FUCHSIA.getColorCode() + ";"));
    }

    /**
     * <p>Method under test: {@link AEMEnvironmentBadgeCRXFilterImpl#doFilter(ServletRequest, ServletResponse, FilterChain)}
     */
    @Test
    void doFilter_AcceptedPathAndPrefixEnabled_ShouldModifyAndWriteContent() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(response.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());

        when(aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix()).thenReturn(true);
        when(aemEnvironmentBadgeConfigService.getDocumentTitlePrefix()).thenReturn("DEV");
        when(aemEnvironmentBadgeConfigService.isEnableBadge()).thenReturn(false);

        when(request.getRequestURI()).thenReturn(CRX_DE_PATH);

        // When the chain.doFilter is called with the wrapped response,
        // we capture that wrapper instance and write the original content to it.
        doAnswer(invocation -> {
            final CharResponseWrapper wrapper = invocation.getArgument(1);
            wrapper.getWriter().write(ORIGINAL_RESPONSE);
            return null;
        }).when(chain).doFilter(eq(request), any(CharResponseWrapper.class));

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(eq(request), any(CharResponseWrapper.class));
        verify(response, times(1)).getWriter();
        verify(response, times(1)).setContentLength(anyInt());

        String finalContent = responseWriter.toString();

        final java.lang.reflect.Field barDivIdField = AEMEnvironmentBadgeCRXFilterImpl.class.getDeclaredField("BAR_DIV_ID");
        barDivIdField.setAccessible(true);
        final String barDivIdValue = (String) barDivIdField.get(null);

        assert (!finalContent.contains("<div id=" + barDivIdValue + "></div>"));
        assert (finalContent.contains("<script>(function(){const t='DEV | '+document.title;document.title=t;let e=0;const n=5,c=1500;"));
        assert (!finalContent.contains("<style>#" + barDivIdValue + "{background-color:" + BackgroundColor.FUCHSIA.getColorCode() + ";"));
    }

    /**
     * <p>Method under test: {@link AEMEnvironmentBadgeCRXFilterImpl#doFilter(ServletRequest, ServletResponse, FilterChain)}
     */
    @Test
    void doFilter_AcceptedPathAndAllEnabled_ShouldModifyAndWriteContent() throws Exception {
        when(response.getWriter()).thenReturn(printWriter);
        when(response.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());

        when(aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix()).thenReturn(true);
        when(aemEnvironmentBadgeConfigService.getDocumentTitlePrefix()).thenReturn("DEV");
        when(aemEnvironmentBadgeConfigService.isEnableBadge()).thenReturn(true);
        when(aemEnvironmentBadgeConfigService.getBadgeBackgroundColor()).thenReturn(BackgroundColor.BLUE.getColor());

        when(request.getRequestURI()).thenReturn(CRX_DE_PATH);

        // When the chain.doFilter is called with the wrapped response,
        // we capture that wrapper instance and write the original content to it.
        doAnswer(invocation -> {
            final CharResponseWrapper wrapper = invocation.getArgument(1);
            wrapper.getWriter().write(ORIGINAL_RESPONSE);
            return null;
        }).when(chain).doFilter(eq(request), any(CharResponseWrapper.class));


        filter.doFilter(request, response, chain);


        verify(chain, times(1)).doFilter(eq(request), any(CharResponseWrapper.class));
        verify(response, times(1)).getWriter();
        verify(response, times(1)).setContentLength(anyInt());

        String finalContent = responseWriter.toString();

        final java.lang.reflect.Field barDivIdField = AEMEnvironmentBadgeCRXFilterImpl.class.getDeclaredField("BAR_DIV_ID");
        barDivIdField.setAccessible(true);
        final String barDivIdValue = (String) barDivIdField.get(null);

        assert (finalContent.contains("<div id=" + barDivIdValue + "></div>"));
        assert (finalContent.contains("<script>(function(){const t='DEV | '+document.title;document.title=t;let e=0;const n=5,c=1500;"));
        assert (finalContent.contains("<style>#" + barDivIdValue + "{background-color:" + BackgroundColor.BLUE.getColorCode() + ";"));
    }

}
