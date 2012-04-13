package com.enonic.cms.core.search.query;

import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MissingFilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.enonic.cms.core.search.IndexType;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

public class TermQueryBuilderFactory
    extends BaseQueryBuilderFactory
{
    public TermQueryBuilderFactory()
    {
    }

    public QueryBuilder buildTermQuery( final QueryPath path, final QueryValue queryValue )
    {
        final boolean isWildCardPath = path.isWildCardPath();

        if ( isWildCardPath )
        {
            path.setMatchAllPath();
        }

        final QueryBuilder termQuery = doBuildTermQuery( path, queryValue, isWildCardPath );

        if ( path.doRenderAsHasChildQuery() )
        {
            return wrapInHasChildQuery( path, termQuery );
        }
        else
        {
            return termQuery;
        }
    }

    private QueryBuilder doBuildTermQuery( QueryPath path, QueryValue queryValue, boolean wildCardPath )
    {
        if ( path.doBuildAsIdQuery() )
        {
            return doRenderIdQuery( queryValue );
        }

        if ( queryValue.isNumeric() )
        {
            return doBuildQueryForNumericValue( path, queryValue, wildCardPath );
        }

        if ( path.isDateField() && queryValue.isEmpty() )
        {
            MissingFilterBuilder filter = FilterBuilders.missingFilter( path.getPath() );
            return QueryBuilders.filteredQuery( matchAllQuery(), filter );
        }
        if ( path.isDateField() && queryValue.isDateTime() )
        {
            return QueryBuilders.termQuery( path.getPath(), queryValue.getDateTime() );
        }

        return QueryBuilders.termQuery( path.getPath(), queryValue.getStringValueNormalized() );
    }

    private QueryBuilder doBuildQueryForNumericValue( final QueryPath path, final QueryValue queryValue, final boolean wildCardPath )
    {
        return QueryBuilders.termQuery( path.getPath(), queryValue.getNumericValue() );
    }

    private QueryBuilder doRenderIdQuery( final QueryValue queryValue )
    {
        if ( queryValue.isNumeric() )
        {
            return QueryBuilders.idsQuery( IndexType.Content.toString() ).addIds( queryValue.getNumericValueAsString() );
        }

        return QueryBuilders.idsQuery( IndexType.Content.toString() ).addIds( queryValue.getStringValueNormalized() );
    }


}