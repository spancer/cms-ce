/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.cms.store.dao;

import java.util.List;
import java.util.SortedMap;

import com.enonic.cms.core.content.category.CategoryEntity;
import com.enonic.cms.core.content.category.CategoryKey;
import com.enonic.cms.core.security.group.GroupKey;
import com.enonic.cms.store.support.EntityPageList;

public interface CategoryDao
    extends EntityDao<CategoryEntity>
{
    void deleteCategory( CategoryEntity category );

    CategoryEntity findByKey( CategoryKey key );

    SortedMap<CategoryKey, CategoryEntity> findByKeys( List<CategoryKey> contentKeys );

    List<CategoryEntity> findRootCategories();

    List<CategoryEntity> findRootCategories( List<GroupKey> groupKeys );

    EntityPageList<CategoryEntity> findAll( int index, int count );

    long countChildrenByCategory( CategoryEntity category );
}
