package com.enonic.cms.core.portal.datasource.handler.extension;

import org.jdom.Document;
import org.springframework.stereotype.Component;

import com.enonic.cms.core.portal.datasource.handler.DataSourceRequest;
import com.enonic.cms.core.portal.datasource.handler.base.ParamDataSourceHandler;

@Component("ds.InvokeExtensionHandler")
public final class InvokeExtensionHandler
    extends ParamDataSourceHandler
{
    public InvokeExtensionHandler()
    {
        super( "invokeExtension" );
    }

    @Override
    public Document handle( final DataSourceRequest req )
        throws Exception
    {
        return null;
    }
}