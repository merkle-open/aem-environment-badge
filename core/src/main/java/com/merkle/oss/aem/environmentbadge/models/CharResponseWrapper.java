package com.merkle.oss.aem.environmentbadge.models;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * A custom {@link HttpServletResponseWrapper} implementation that captures
 * character-based response output for later inspection or modification.
 * <p>
 * This wrapper intercepts calls to {@link #getWriter()} and stores all written
 * text in an internal {@link java.io.CharArrayWriter}. It is typically used in
 * servlet filters where the response content needs to be examined, transformed,
 * or appended before being sent to the client.
 * </p>
 *
 * <h3>Usage Example</h3>
 * <pre>{@code
 * CharResponseWrapper wrappedResponse = new CharResponseWrapper(response);
 * filterChain.doFilter(request, wrappedResponse);
 * String output = wrappedResponse.getCapturedOutput();
 * // modify output...
 * response.getWriter().write(output);
 * }</pre>
 *
 * <h3>Notes</h3>
 * <ul>
 *   <li>This wrapper only supports character output via {@link #getWriter()}.
 *       It does not override {@link #getOutputStream()}; therefore it should
 *       only be used for text-based responses (HTML, JSON, XML, etc.).</li>
 *   <li>The captured content is stored in memory, so extremely large responses
 *       should be avoided.</li>
 *   <li>The wrapper does not automatically write data back to the original
 *       response; users of this class must handle that manually.</li>
 * </ul>
 */
public class CharResponseWrapper extends HttpServletResponseWrapper {

    private final CharArrayWriter writer = new CharArrayWriter();

    /**
     * Creates a new response wrapper that will capture all character output.
     *
     * @param response the original {@link HttpServletResponse} to wrap
     * @throws IllegalArgumentException if {@code response} is {@code null}
     */
    public CharResponseWrapper(final HttpServletResponse response) {
        super(response);
    }

    /**
     * Returns a {@link PrintWriter} that writes into an internal buffer
     * instead of directly to the underlying HTTP response.
     *
     * @return a writer that captures character data
     */
    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(writer);
    }

    /**
     * Returns all content written to this wrapper's writer so far.
     *
     * @return the captured response output as a string, never {@code null}
     */
    public String getCapturedOutput() {
        return writer.toString();
    }

}