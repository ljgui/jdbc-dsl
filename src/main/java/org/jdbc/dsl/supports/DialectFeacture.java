package org.jdbc.dsl.supports;

import lombok.Getter;
import org.jdbc.dsl.core.Feature;
import org.jdbc.dsl.core.FeatureSupportedMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: ljgui
 */
public abstract class DialectFeacture implements FeatureSupportedMetadata{

    @Getter
    protected Map<String, Feature> features = new HashMap<>();

}
