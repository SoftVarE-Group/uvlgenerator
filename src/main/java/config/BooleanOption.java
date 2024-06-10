package config;

import java.util.Random;

public class BooleanOption implements ConfigurationOption<Boolean> {

    private final String optionName;
    private final Boolean optionValue;

    public BooleanOption(String optionName, Boolean optionValue) {
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public Boolean getNextValue(Random random) {
        return getStaticValue();
    }

    @Override
    public Boolean getStaticValue() {
        return optionValue;
    }
}
