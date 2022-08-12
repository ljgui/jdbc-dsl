package org.jdbc.dsl.supports;

import lombok.Getter;
import org.jdbc.dsl.core.Feature;
import org.jdbc.dsl.core.FeatureSupportedMetadata;
import org.jdbc.dsl.core.FeatureType;
import org.jdbc.dsl.supports.mysql.MysqlDialect;
import org.jdbc.dsl.supports.postgresql.PostgresqlDialect;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: ljgui
 */
public class Dialects implements FeatureSupportedMetadata {

    Map<FeatureType,Feature> features =  new HashMap<>();

    public Dialects(){
        addFeature(MysqlDialect.getInstance());
        addFeature(PostgresqlDialect.getInstance());
    }

    @Override
    public Map<String, Feature> getFeatures() {
        return features.entrySet().stream().collect(Collectors.toMap(k -> k.getKey().getId(), v -> v.getValue()));
    }

    @Override
    public void addFeature(Feature feature) {
        features.put(feature.getType(),feature);
    }

    private static Dialects instance = new Dialects();
    public static Dialects of(){
        return instance;
    }

}
