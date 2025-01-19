package config.constraints;

import com.eclipsesource.json.JsonObject;
import config.helper.ConfigurationOption;
import config.helper.DoubleOption;
import config.helper.DoubleRangeOption;
import config.distribution.DistributionOption;

import java.util.HashMap;
import java.util.Map;

public class ConstraintTypeOption extends DistributionOption<ConstraintTypeOption.ConstraintType> {

    private static Map<String, double[]> defaultValues;

    static {
        defaultValues = new HashMap<>();
        defaultValues.put("boolean", new double[] {0.4, 0.95});
        defaultValues.put("numeric", new double[] {0.0, 0.2});
        defaultValues.put("aggregate", new double[] {0.0, 0.1});
        defaultValues.put("string", new double[] {0.0, 0.0});

    }


    public static ConstraintTypeOption fromJson(JsonObject distribution) {
        if (distribution.isNull() || distribution.isEmpty()) {
            return createDefault();
        }
        Map<ConstraintType, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(ConstraintType.BOOLEAN, DoubleOption.parseDoubleOptionJson("boolean", distribution.get("boolean"), defaultValues.get("boolean")));
        options.put(ConstraintType.NUMERIC, DoubleOption.parseDoubleOptionJson("numeric", distribution.get("numeric"), defaultValues.get("numeric")));
        options.put(ConstraintType.AGGREGATE, DoubleOption.parseDoubleOptionJson("aggregate", distribution.get("aggregate"), defaultValues.get("aggregate")));
        options.put(ConstraintType.STRING, DoubleOption.parseDoubleOptionJson("string", distribution.get("string"), defaultValues.get("string")));
        return new ConstraintTypeOption(options);
    }

    public static ConstraintTypeOption createDefault() {
        Map<ConstraintType, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(ConstraintType.BOOLEAN, new DoubleRangeOption("boolean", 0.4, 0.95));
        options.put(ConstraintType.NUMERIC, new DoubleRangeOption("numeric", 0.0, 0.5));
        options.put(ConstraintType.AGGREGATE, new DoubleRangeOption("aggregate", 0.0, 0.2));
        options.put(ConstraintType.STRING, new DoubleRangeOption("string", 0.0, 0.1));
        return new ConstraintTypeOption(options);
    }

    public ConstraintTypeOption(Map<ConstraintType, ConfigurationOption<Double>> distribution) {
        super("constraintType", distribution);
    }

    @Override
    public ConstraintType fromString(String value) {
        return ConstraintType.fromString(value);
    }


    public enum ConstraintType {
        BOOLEAN("Boolean"),
        NUMERIC("Numeric"),
        AGGREGATE("Aggregate"),
        STRING("String");

        final String name;

        ConstraintType(String name) {
            this.name = name;
        }

        public static ConstraintType fromString(final String name) {
            for (final ConstraintType featureType : ConstraintType.values()) {
                if (featureType.name.equalsIgnoreCase(name)) {
                    return featureType;
                }
            }
            return null;
        }
    }


}
