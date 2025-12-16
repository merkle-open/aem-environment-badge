package com.merkle.oss.aem.environmentbadge.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.merkle.oss.aem.environmentbadge.constants.BackgroundColor;
import com.merkle.oss.aem.environmentbadge.services.AEMEnvironmentBadgeConfigService;
import com.merkle.oss.aem.environmentbadge.services.impl.AEMEnvironmentBadgeConfigServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link AEMEnvironmentBadgeConfigServlet} class.
 */
@ExtendWith(AemContextExtension.class)
class AEMEnvironmentBadgeConfigServletTest {

    private final AEMEnvironmentBadgeConfigServlet fixture = new AEMEnvironmentBadgeConfigServlet();

    /**
     * <p>Method under test: helper to inject a value into a private field via reflection.
     *
     * <p>This is used to wire OSGi {@code @Reference}-style dependencies into the servlet instance
     * in a plain unit test (without starting a real OSGi container).
     *
     * @param target    Object whose field should be set.
     * @param fieldName Name of the field to set.
     * @param value     Value to assign to the field.
     * @throws IllegalStateException if the field does not exist or cannot be accessed.
     */
    private static void injectField(final Object target, final String fieldName, final Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Field '" + fieldName + "' not found on " + target.getClass().getName()
                    + ". Consider registering the servlet as an OSGi component in the test instead.", e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot inject field '" + fieldName + "' on " + target.getClass().getName(), e);
        }
    }

    /**
     * <p>Method under test: helper to compare two JSON strings structurally.
     *
     * <p>This avoids brittle assertions that can fail due to key ordering or whitespace differences
     * even when the JSON documents are semantically identical.
     *
     * @param expectedJson Expected JSON string.
     * @param actualJson   Actual JSON string produced by the servlet.
     */
    private static void assertJsonEquals(final String expectedJson, final String actualJson) {
        final JsonElement expected = JsonParser.parseString(expectedJson);
        final JsonElement actual = JsonParser.parseString(actualJson);
        assertEquals(expected, actual);
    }

    /**
     * <p>Method under test: {@link AEMEnvironmentBadgeConfigServlet#doGet(SlingHttpServletRequest, SlingHttpServletResponse)}.
     */
    @Test
    void doGet_emptyObject(final AemContext context) throws IOException {
        final MockSlingHttpServletRequest request = context.request();
        final MockSlingHttpServletResponse response = context.response();

        fixture.doGet(request, response);

        assertJsonEquals("{}", response.getOutputAsString());
    }

    /**
     * <p>Method under test: {@link AEMEnvironmentBadgeConfigServlet#doGet(SlingHttpServletRequest, SlingHttpServletResponse)}.
     */
    @Test
    void doGet_settingsObject(final AemContext context) throws IOException {
        final MockSlingHttpServletRequest request = context.request();
        final MockSlingHttpServletResponse response = context.response();

        final Map<String, Object> configurationDto = new HashMap<>();
        configurationDto.put("enableDocumentTitlePrefix", true);
        configurationDto.put("documentTitlePrefix", "prefix");
        configurationDto.put("enableBadge", true);
        configurationDto.put("badgeBackgroundColor", BackgroundColor.FUCHSIA.getColor());
        configurationDto.put("badgeTitle", "title");
        final String expectedJson = new Gson().toJson(configurationDto);

        final Map<String, Object> config = Map.of(
                "enableDocumentTitlePrefix", true,
                "documentTitlePrefix", "prefix",
                "enableBadge", true,
                "badgeBackgroundColor", BackgroundColor.FUCHSIA.getColor(),
                "badgeTitle", "title"
        );

        final AEMEnvironmentBadgeConfigService service = context.registerInjectActivateService(new AEMEnvironmentBadgeConfigServiceImpl(), config);
        injectField(fixture, "aemEnvironmentBadgeConfigService", service);
        fixture.doGet(request, response);

        assertJsonEquals(expectedJson, response.getOutputAsString());
    }

}
