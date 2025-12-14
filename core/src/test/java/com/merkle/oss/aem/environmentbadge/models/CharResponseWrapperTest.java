package com.merkle.oss.aem.environmentbadge.models;

import org.apache.jackrabbit.webdav.WebdavResponseImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CharResponseWrapper} class.
 */
@ExtendWith(MockitoExtension.class)
class CharResponseWrapperTest {

    @Mock
    private HttpServletResponse response;

    /**
     * <p>Method under test: {@link CharResponseWrapper#CharResponseWrapper(HttpServletResponse)}
     */
    @Test
    void testNewCharResponseWrapper() {
        assertThrows(IllegalArgumentException.class, () -> new CharResponseWrapper(null));
        assertEquals(new CharResponseWrapper(response).getResponse(), response);
    }

    /**
     * <p>Method under test: {@link CharResponseWrapper#getWriter()}
     */
    @Test
    void testGetWriter_thenReturnNotCheckError() {
        assertFalse(new CharResponseWrapper(new WebdavResponseImpl(null)).getWriter().checkError());
    }

    /**
     * <p>Method under test: {@link CharResponseWrapper#getCapturedOutput()}
     */
    @Test
    void testGetCapturedOutput_thenReturnEmptyString() {
        assertEquals("", new CharResponseWrapper(new WebdavResponseImpl(null)).getCapturedOutput());
    }

}
