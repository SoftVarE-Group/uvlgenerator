package config.tree;

import com.eclipsesource.json.JsonObject;
import config.helper.ConfigurationOption;
import config.helper.DoubleOption;
import config.helper.DoubleRangeOption;
import config.distribution.DistributionOption;
import de.vill.model.FeatureType;

import java.util.HashMap;
import java.util.Map;

public class FeatureTypeOption extends DistributionOption<FeatureType> {

    private static final String name = "featureType";

    public static FeatureTypeOption createDefault() {
        Map<FeatureType, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(FeatureType.BOOL, new DoubleRangeOption("boolean",  0.7,0.99));
        options.put(FeatureType.INT, new DoubleRangeOption("integer", 0.0, 0.2));
        options.put(FeatureType.REAL, new DoubleRangeOption("real", 0.0, 0.2));
        options.put(FeatureType.STRING, new DoubleRangeOption("string", 0.0, 0.1));
        return new FeatureTypeOption(options);
    }

    public static FeatureTypeOption fromJson(JsonObject distribution) {
        if (distribution.isNull() || distribution.isEmpty()) {
            return createDefault();
        }
        Map<FeatureType, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(FeatureType.BOOL, DoubleOption.parseDoubleOptionJson("boolean", distribution.get("boolean"), new double[] {0.9}));
        options.put(FeatureType.INT, DoubleOption.parseDoubleOptionJson("integer", distribution.get("integer"), new double[] {0.05}));
        options.put(FeatureType.REAL, DoubleOption.parseDoubleOptionJson("real", distribution.get("real"), new double[] {0.04}));
        options.put(FeatureType.STRING, DoubleOption.parseDoubleOptionJson("string", distribution.get("string"), new double[] {0.01}));
        return new FeatureTypeOption(options);
    }

    public FeatureTypeOption(Map<FeatureType, ConfigurationOption<Double>> distribution) {
        super(name, distribution);
    }

    @Override
    public FeatureType fromString(String value) {
        return FeatureType.fromString(value);
    }
}
