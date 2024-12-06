package config;

import com.eclipsesource.json.JsonObject;
import de.vill.model.FeatureType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FeatureTypeOption extends DistributionOption<FeatureType> {

    private static final String name = "featureType";

    public static FeatureTypeOption createDefault() {
        Map<FeatureType, Double> options = new HashMap<>();
        options.put(FeatureType.BOOL, 0.9);
        options.put(FeatureType.INT, 0.05);
        options.put(FeatureType.REAL, 0.04);
        options.put(FeatureType.STRING, 0.01);
        return new FeatureTypeOption(options);
    }

    public static FeatureTypeOption createRandom(Random rng) {
        Map<FeatureType, Double> options = new HashMap<>();
        double next = (double) rng.nextInt(0,10) / 10;
        double assigned = next;
        options.put(FeatureType.BOOL, next);
        next = computeNext(assigned, rng);
        assigned += next;
        options.put(FeatureType.INT, next);
        next = computeNext(assigned, rng);
        assigned += next;
        options.put(FeatureType.REAL, next);
        next = computeNext(assigned, rng);
        options.put(FeatureType.STRING, next);
        return new FeatureTypeOption(options);
    }

    private static double computeNext(double assigned, Random rng) {
        return (double) rng.nextInt(0,(int) (1.0 - assigned) * 10) / 10;
    }

    public static FeatureTypeOption fromJson(JsonObject distribution) {
        if (distribution.isNull() || distribution.isEmpty()) {
            return createDefault();
        }
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
