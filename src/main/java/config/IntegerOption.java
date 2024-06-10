package config;

import java.util.Random;

public class IntegerOption implements ConfigurationOption<Integer> {

    public int optionValue;
    public String optionName;

    public IntegerOption(int optionValue, String optionName) {
        this.optionValue = optionValue;
        this.optionName = optionName;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public Integer getNextValue(Random random) {
        return getStaticValue();
    }

    @Override
    public Integer getStaticValue() {
        return optionValue;
    }

    @Override
    public void initValue(Random random) {}
}
