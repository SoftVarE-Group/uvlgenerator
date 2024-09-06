package config;

import java.util.Random;

public class IntegerRangeOption implements ConfigurationOption<Integer>{

    private final String optionName;
    private final int lower;
    private final int upper;

    public IntegerRangeOption(String optionName, int lower, int upper) {
        this.optionName = optionName;
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public Integer getNextValue(Random random) {
        return random.nextInt(upper - lower + 1) + lower;
    }

    @Override
    public Integer getStaticValue() {
        return (upper + lower) / 2;
    }
}
