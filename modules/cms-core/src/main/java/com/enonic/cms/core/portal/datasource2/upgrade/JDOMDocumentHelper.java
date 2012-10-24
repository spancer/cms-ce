package com.enonic.cms.core.portal.datasource2.upgrade;

import java.util.List;

import org.jdom.Element;

import com.google.common.collect.Lists;

final class JDOMDocumentHelper
{
    public static Element findElement( final Element parent, final String name )
    {
        final List<Element> list = findElements( parent, name );
        return list.isEmpty() ? null : list.get( 0 );
    }

    public static List<Element> findElements( final Element parent, final String name )
    {
        final List<Element> list = Lists.newArrayList();

        if ( parent != null )
        {
            for ( final Object o : parent.getContent( new ElementNameFilter( name ) ) )
            {
                list.add( (Element) o );
            }
        }

        return list;
    }

    public static String getTextNode( final Element parent )
    {
        if ( parent != null )
        {
            return parent.getTextNormalize().trim();
        }
        else
        {
            return null;
        }
    }
}
