package config;

import com.eclipsesource.json.JsonObject;
import de.vill.model.Cardinality;

import java.util.Random;

public class FeatureCardinalityOption implements ConfigurationOption<Cardinality> {

    private final ConfigurationOption<Integer> min;
    private final ConfigurationOption<Integer> max;
    private final double attachProbability;

    public FeatureCardinalityOption(ConfigurationOption<Integer> min, ConfigurationOption<Integer> max, double attachProbability) {
        this.min = min;
        this.max = max;
        this.attachProbability = attachProbability;
    }

    public static FeatureCardinalityOption fromJson(JsonObject json) {
        ConfigurationOption<Integer> min = IntegerOption.parseIntegerOption("min", json.get("min"));
        ConfigurationOption<Integer> max = IntegerOption.parseIntegerOption("max", json.get("max"));
        double attachProbability = json.get("attachProbability").asDouble();
        return new FeatureCardinalityOption(min, max, attachProbability);
    }


    @Override
    public String getOptionName() {
        return "featureCardinality";
    }

    @Override
    public Cardinality getNextValue(Random random) {
        if (random.nextDouble() > attachProbability) {
            return null;
        }
        return new Cardinality(min.getNextValue(random), max.getNextValue(random));
    }

    @Override
    public Cardinality getStaticValue() {
        return null;
    }
}
