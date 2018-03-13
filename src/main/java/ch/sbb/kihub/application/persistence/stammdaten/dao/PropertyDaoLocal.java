/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2018.
 */
package ch.sbb.kihub.application.persistence.stammdaten.dao;

import java.util.Map;

import javax.ejb.Local;

import ch.sbb.kihub.application.persistence.stammdaten.entity.PropertyEntity;

/**
 * Stellt alle Methoden zur Verfügung, um die {@link PropertyEntity}s zu verwalten.
 * 
 * @author u203378 (Stadler Lukas)
 * @version $Id$
 */
@Local
public interface PropertyDaoLocal {

    /**
     * Liefert das entsprechende Value zum gesuchten Key aus der Property-Tabelle.
     * 
     * @param key String welcher den entsprechenden Key Enthält
     * @return Value des entsprechenden Properties
     * @throws NullPointerException DOCUMENT ME!
     */
    String findPropertyValueByKey(String key);

    /**
     * Liefert das entsprechende Property zum gesuchten Key aus der Property-Tabelle.
     * 
     * @param key String welcher den entsprechenden Key Enthält
     * @return entsprechendes Property
     */
    PropertyEntity findPropertyByKey(String key);

    /**
     * Liefert alle Einträge der Tabelle Properties
     * 
     * @return
     */
    Map<String, PropertyEntity> getAllProperties();

}
