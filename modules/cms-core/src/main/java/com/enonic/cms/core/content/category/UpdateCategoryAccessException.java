/*
 * Copyright 2000-2013 Enonic AS
 * http://www.enonic.com/license
 */

package com.enonic.cms.core.content.category;

import com.enonic.cms.core.security.user.QualifiedUsername;

public class UpdateCategoryAccessException
    extends RuntimeException
{
    public UpdateCategoryAccessException( String reason, QualifiedUsername user )
    {
        super( buildMessage( reason, user ) );
    }

    private static String buildMessage( String reason, QualifiedUsername user )
    {
        StringBuilder msg = new StringBuilder();
        msg.append( " User " ).append( user ).append( " do not have access to update category: " ).append( reason );
        return msg.toString();
    }
}
