package config;

import com.eclipsesource.json.JsonValue;

import java.util.Random;

public class DoubleOption implements ConfigurationOption<Double> {

    private final String optionName;
    private final Double value;

    public DoubleOption(String optionName, Double value) {
        this.optionName = optionName;
        this.value = value;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public Double getNextValue(Random random) {
        return getStaticValue();
    }

    @Override
    public Double getStaticValue() {
        return value;
    }


    public static ConfigurationOption<Double> parseDoubleOption(String name, JsonValue value) {
        if (value.isArray()) {
            return new DoubleRangeOption(name, value.asArray().get(0).asDouble(), value.asArray().get(1).asDouble());
        } else if (value.isNumber()) {
            return new DoubleOption(name, value.asDouble());
        } else {
            return null;
        }
    }
}
