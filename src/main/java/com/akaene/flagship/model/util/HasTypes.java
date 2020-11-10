package com.akaene.flagship.model.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public interface HasTypes {

    Set<String> getTypes();

    void setTypes(Set<String> types);

    default void addType(String type) {
        Objects.requireNonNull(type);
        if (getTypes() == null) {
            setTypes(new HashSet<>(4));
        }
        getTypes().add(type);
    }
}
