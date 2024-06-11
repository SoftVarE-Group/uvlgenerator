package config;

import com.eclipsesource.json.JsonObject;
import de.vill.model.FeatureType;

import java.util.HashMap;
import java.util.Map;

public class FeatureTypeOption extends DistributionOption<FeatureType> {

    private static final String name = "featureType";

    public static FeatureTypeOption fromJson(JsonObject distribution) {
        Map<FeatureType, Double> options = new HashMap<>();
        options.put(FeatureType.BOOL, distribution.get("boolean").asDouble());
        options.put(FeatureType.INT, distribution.get("integer").asDouble());
        options.put(FeatureType.REAL, distribution.get("real").asDouble());
        options.put(FeatureType.STRING, distribution.get("string").asDouble());
        return new FeatureTypeOption(options);
    }

    public FeatureTypeOption(Map<FeatureType, Double> distribution) {
        super(name, distribution);
    }

    @Override
    public FeatureType fromString(String value) {
        return FeatureType.fromString(value);
    }
}
