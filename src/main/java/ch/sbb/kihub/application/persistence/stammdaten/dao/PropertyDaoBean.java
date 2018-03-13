/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2018.
 */
package ch.sbb.kihub.application.persistence.stammdaten.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import ch.sbb.kihub.application.persistence.stammdaten.entity.PropertyEntity;

/**
 * Session Bean um auf DB-Properties zu lesen.
 * 
 * @author u203378 (Stadler Lukas)
 * @version $Id$
 */
@Stateless
@Resource(name = "jdbc/kihub/oracle", type = DataSource.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PropertyDaoBean implements PropertyDaoLocal {

    @PersistenceContext(unitName = "kihub-pu")
    private EntityManager entityManager;

    @Override
    public String findPropertyValueByKey(final String key) {
        String value = null;

        final PropertyEntity property = findPropertyByKey(key);
        if (property != null) {
            value = property.getValue();
        }
        return value;
    }

    @Override
    public PropertyEntity findPropertyByKey(final String key) {
        // Da alle Properties gecached werden, kann dies so gemacht werden.
        final PropertyEntity dbProp = getAllProperties().get(key);

        return dbProp;
    }

    @Override
    public Map<String, PropertyEntity> getAllProperties() {

        final TypedQuery<PropertyEntity> namedQuery = entityManager.createNamedQuery("FIND_ALL_PROPERTIES", PropertyEntity.class);
        final List<PropertyEntity> properties = namedQuery.getResultList();

        final Map<String, PropertyEntity> propertyKeyMap = new HashMap<>();

        for (final PropertyEntity propertyEntity : properties) {
            propertyKeyMap.put(propertyEntity.getKey(), propertyEntity);
        }

        return propertyKeyMap;
    }
}
