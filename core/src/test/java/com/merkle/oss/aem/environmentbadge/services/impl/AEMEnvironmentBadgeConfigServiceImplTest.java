package com.merkle.oss.aem.environmentbadge.services.impl;

import com.merkle.oss.aem.environmentbadge.constants.BackgroundColor;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link AEMEnvironmentBadgeConfigServiceImpl} class.
 */
@ExtendWith(MockitoExtension.class)
public class AEMEnvironmentBadgeConfigServiceImplTest {

    @Mock
    private AEMEnvironmentBadgeConfigServiceImpl.AEMEnvironmentBadgeConfig config;

    @InjectMocks
    private AEMEnvironmentBadgeConfigServiceImpl aemEnvironmentBadgeConfigService = new AEMEnvironmentBadgeConfigServiceImpl();

    /**
     * Method under test: {@link AEMEnvironmentBadgeConfigServiceImpl#activate(AEMEnvironmentBadgeConfigServiceImpl.AEMEnvironmentBadgeConfig)}
     */
    @Test
    void testActivate() {
        assertThrows(NullPointerException.class, () -> aemEnvironmentBadgeConfigService.activate(null));
        assertDoesNotThrow(() -> aemEnvironmentBadgeConfigService.activate(config));
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link AEMEnvironmentBadgeConfigServiceImpl#isEnableDocumentTitlePrefix()}
     *   <li>{@link AEMEnvironmentBadgeConfigServiceImpl#getDocumentTitlePrefix()}
     *   <li>{@link AEMEnvironmentBadgeConfigServiceImpl#isEnableBadge()}
     *   <li>{@link AEMEnvironmentBadgeConfigServiceImpl#getBadgeBackgroundColor()}
     *   <li>{@link AEMEnvironmentBadgeConfigServiceImpl#getBadgeTitle()}
     * </ul>
     */
    @Test
    void testGetters_PrefixTitleSet() {
        when(config.enableDocumentTitlePrefix()).thenReturn(true);
        when(config.documentTitlePrefix()).thenReturn("prefix");
        when(config.enableBadge()).thenReturn(true);
        when(config.badgeBackgroundColor()).thenReturn(BackgroundColor.BLUE.getColor());
        when(config.badgeTitle()).thenReturn("title");

        assertTrue(aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix());
        assertEquals("prefix", aemEnvironmentBadgeConfigService.getDocumentTitlePrefix());
        assertTrue(aemEnvironmentBadgeConfigService.isEnableBadge());
        assertEquals(BackgroundColor.BLUE.getColor(), aemEnvironmentBadgeConfigService.getBadgeBackgroundColor());
        assertEquals("title", aemEnvironmentBadgeConfigService.getBadgeTitle());

        when(config.enableDocumentTitlePrefix()).thenReturn(false);
        assertFalse(aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix());

        when(config.enableDocumentTitlePrefix()).thenReturn(true);
        when(config.documentTitlePrefix()).thenReturn(null);
        assertFalse(aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix());
    }

    /**
     * Method under test: {@link AEMEnvironmentBadgeConfigServiceImpl#isEnableDocumentTitlePrefix()}
     */
    @Test
    void testGetters_PrefixTitleNotSet() {
        when(config.enableDocumentTitlePrefix()).thenReturn(true);
        when(config.documentTitlePrefix()).thenReturn(StringUtils.EMPTY);

        assertFalse(aemEnvironmentBadgeConfigService.isEnableDocumentTitlePrefix());
    }

    /**
     * Methods under test:
     * <ul>
     *   <li>{@link AEMEnvironmentBadgeConfigServiceImpl#isEnableBadge()}
     *   <li>{@link AEMEnvironmentBadgeConfigServiceImpl#getBadgeBackgroundColor()}
     *   <li>{@link AEMEnvironmentBadgeConfigServiceImpl#getBadgeTitle()}
     * </ul>
     */
    @Test
    void testGetters_BadgeTitleNotSet() {
        when(config.enableBadge()).thenReturn(true);
        when(config.badgeBackgroundColor()).thenReturn(BackgroundColor.BLUE.getColor());
        when(config.badgeTitle()).thenReturn(StringUtils.EMPTY);

        assertTrue(aemEnvironmentBadgeConfigService.isEnableBadge());
        assertEquals(BackgroundColor.BLUE.getColor(), aemEnvironmentBadgeConfigService.getBadgeBackgroundColor());
        assertEquals(StringUtils.EMPTY, aemEnvironmentBadgeConfigService.getBadgeTitle());
    }

}
