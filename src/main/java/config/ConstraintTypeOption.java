package config;

import com.eclipsesource.json.JsonObject;
import de.vill.model.FeatureType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ConstraintTypeOption extends DistributionOption<ConstraintTypeOption.ConstraintType> {

    public static ConstraintTypeOption fromJson(JsonObject distribution) {
        Map<ConstraintType, Double> options = new HashMap<>();
        options.put(ConstraintType.BOOLEAN, distribution.get("boolean").asDouble());
        options.put(ConstraintType.NUMERIC, distribution.get("numeric").asDouble());
        options.put(ConstraintType.AGGREGATE, distribution.get("aggregate").asDouble());
        options.put(ConstraintType.STRING, distribution.get("string").asDouble());
        return new ConstraintTypeOption(options);
    }

    public ConstraintTypeOption(Map<ConstraintType, Double> distribution) {
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
