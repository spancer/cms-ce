package com.enonic.cms.web.filter;

import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import junit.framework.TestCase;

public class HttpHeadFilterTest
    extends TestCase
{
    private MockHttpServletRequest request = new MockHttpServletRequest();

    private MockHttpServletResponse response = new MockHttpServletResponse();

    private MockFilterChain chain = new MockFilterChain();

    public void testPOST()
        throws Exception
    {
        request.setProtocol( "http" );
        request.setServerName( "localhost" );
        request.setCharacterEncoding( "UTF-8" );
        request.setMethod( "POST" );

        final HttpHeadFilter filter = new HttpHeadFilter();
        filter.doFilter( request, response, chain );

        final MockHttpServletRequest result = (MockHttpServletRequest) chain.getRequest();

        assertEquals( request, result );
    }

    public void testHEAD_Response()
        throws Exception
    {
        request.setProtocol( "http" );
        request.setServerName( "localhost" );
        request.setCharacterEncoding( "UTF-8" );
        request.setMethod( "HEAD" );

        final HttpHeadFilter filter = new HttpHeadFilter();
        filter.doFilter( request, response, chain );

        final HttpHeadFilter.NoBodyResponseWrapper result = (HttpHeadFilter.NoBodyResponseWrapper) chain.getResponse();
        final HttpHeadFilter.NoBodyOutputStream stream = (HttpHeadFilter.NoBodyOutputStream) result.getOutputStream();

        assertEquals( 0, stream.getContentLength() );
    }

    public void testHEAD_Request()
        throws Exception
    {
        request.setProtocol( "http" );
        request.setServerName( "localhost" );
        request.setCharacterEncoding( "UTF-8" );
        request.setMethod( "HEAD" );

        final HttpHeadFilter filter = new HttpHeadFilter();
        filter.doFilter( request, response, chain );

        final HttpHeadFilter.ForceGetRequestWrapper result = (HttpHeadFilter.ForceGetRequestWrapper) chain.getRequest();

        assertEquals( "GET", result.getMethod() );
    }
}
