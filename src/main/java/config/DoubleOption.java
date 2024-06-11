package config;

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
}
