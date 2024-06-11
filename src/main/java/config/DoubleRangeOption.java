package config;

import java.util.Random;

public class DoubleRangeOption implements ConfigurationOption<Double> {

    private final String optionName;
    private final Double lower;
    private final Double upper;

    public DoubleRangeOption(String optionName, Double lower, Double upper) {
        this.optionName = optionName;
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public Double getNextValue(Random random) {
        return random.nextDouble() * (upper - lower) + lower;
    }

    @Override
    public Double getStaticValue() {
        return lower;
    }
}
