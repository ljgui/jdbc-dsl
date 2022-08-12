package org.jdbc.dsl.core;

public interface FeatureId<T extends Feature> {

    String getId();

    static <T extends Feature> FeatureId<T> of(String id) {
        return () -> id;
    }
}
