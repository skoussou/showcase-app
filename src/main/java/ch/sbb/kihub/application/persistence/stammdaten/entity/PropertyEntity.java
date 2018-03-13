/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2018.
 */
package ch.sbb.kihub.application.persistence.stammdaten.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Property Entity. Hat über die Annotation @DataCache(timeout=3600000) caching aktiviert welches sich jeweils stündlich die neuen Werte aus der DB
 * holt.
 * 
 * @author u203378 (Stadler Lukas)
 * @since 0.11.0
 */
@Entity
@Table(name = "Property")
public class PropertyEntity implements Serializable{
    private static final long serialVersionUID = -7729980796413640619L;

    private static final String TOSTRING_FORMAT_DEFINITION = "Property [key=%s, value=%s]";

    @Id
    private String key;

    private String beschreibung;

    private String value;

    /**
     * Konstruktor.
     */
    public PropertyEntity() {
    }

    public String getKey() {
        return this.key;
    }

    public String getBeschreibung() {
        return this.beschreibung;
    }

    public String getValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT_DEFINITION, key, value);
    }
}
