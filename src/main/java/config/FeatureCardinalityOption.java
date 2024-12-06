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

    public static FeatureCardinalityOption createDefault(Random rng) {
        ConfigurationOption<Integer> min = IntegerOption.parseIntegerOption("min", new int[] {1,50});
        ConfigurationOption<Integer> max = IntegerOption.parseIntegerOption("max", new int[] {1,50});
        double attachProbability = (double) rng.nextInt(20) / 100;
        return new FeatureCardinalityOption(min, max, attachProbability);
    }

    public static FeatureCardinalityOption fromJson(JsonObject json) {
        ConfigurationOption<Integer> min = IntegerOption.parseIntegerOptionJson("min", json.get("min"), new int[] {1,50});
        ConfigurationOption<Integer> max = IntegerOption.parseIntegerOptionJson("max", json.get("max"), new int[] {1,50});
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
        int minValue;
        int maxValue;
        do {
            minValue = min.getNextValue(random);
            maxValue = max.getNextValue(random);
        } while (minValue > maxValue);

        return new Cardinality(minValue, maxValue);
    }

    @Override
    public Cardinality getStaticValue() {
        return null;
    }
}
