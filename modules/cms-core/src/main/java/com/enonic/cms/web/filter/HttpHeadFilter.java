package com.enonic.cms.web.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.web.filter.GenericFilterBean;

/**
 * Servlet filter that presents a HEAD request as a GET. The application doesn't need to know the difference, as this
 * filter handles all the details.
 */
public final class HttpHeadFilter
    extends GenericFilterBean
{
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
        throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if ( isHttpHead( httpServletRequest ) )
        {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            NoBodyResponseWrapper noBodyResponseWrapper = new NoBodyResponseWrapper( httpServletResponse );

            chain.doFilter( new ForceGetRequestWrapper( httpServletRequest ), noBodyResponseWrapper );
            noBodyResponseWrapper.setContentLength();
        }
        else
        {
            chain.doFilter( request, response );
        }
    }

    /**
     * Checks whether the HTTP method of this request is HEAD.
     *
     * @param request The request to check.
     * @return {@code true} if it is HEAD, {@code false} if it isn't.
     */
    private boolean isHttpHead( HttpServletRequest request )
    {
        return "HEAD".equals( request.getMethod() );
    }

    /**
     * Request wrapper that lies about the Http method and always returns GET.
     */
    final class ForceGetRequestWrapper
        extends HttpServletRequestWrapper
    {
        /**
         * Initializes the wrapper with this request.
         *
         * @param request The request to initialize the wrapper with.
         */
        ForceGetRequestWrapper( HttpServletRequest request )
        {
            super( request );
        }

        /**
         * Lies about the HTTP method. Always returns GET.
         *
         * @return Always returns GET.
         */
        @Override
        public String getMethod()
        {
            return "GET";
        }
    }

    /**
     * Response wrapper that swallows the response body, leaving only the headers.
     */
    final class NoBodyResponseWrapper
        extends HttpServletResponseWrapper
    {
        /**
         * Outputstream that discards the data written to it.
         */
        private final NoBodyOutputStream noBodyOutputStream = new NoBodyOutputStream();

        private PrintWriter writer;

        /**
         * Constructs a response adaptor wrapping the given response.
         *
         * @param response The response to wrap.
         */
        NoBodyResponseWrapper( HttpServletResponse response )
        {
            super( response );
        }

        @Override
        public ServletOutputStream getOutputStream()
            throws IOException
        {
            return noBodyOutputStream;
        }

        @Override
        public PrintWriter getWriter()
            throws UnsupportedEncodingException
        {
            if ( writer == null )
            {
                writer = new PrintWriter( new OutputStreamWriter( noBodyOutputStream, getCharacterEncoding() ) );
            }

            return writer;
        }

        /**
         * Sets the content length, based on what has been written to the outputstream so far.
         */
        void setContentLength()
        {
            super.setContentLength( noBodyOutputStream.getContentLength() );
        }
    }

    /**
     * Outputstream that only counts the length of what is being written to it while discarding the actual data.
     */
    final class NoBodyOutputStream
        extends ServletOutputStream
    {
        /**
         * The number of bytes written to this stream so far.
         */
        private int contentLength = 0;

        /**
         * @return The number of bytes written to this stream so far.
         */
        int getContentLength()
        {
            return contentLength;
        }

        @Override
        public void write( int b )
        {
            contentLength++;
        }

        @Override
        public void write( byte buf[], int offset, int len )
            throws IOException
        {
            contentLength += len;
        }
    }
}
