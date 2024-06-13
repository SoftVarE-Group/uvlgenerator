package config;

import com.eclipsesource.json.JsonValue;

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

    public static ConfigurationOption<Integer> parseIntegerOption(String name, JsonValue value) {
        if (value.isArray()) {
            return new IntegerRangeOption(name, value.asArray().get(0).asInt(), value.asArray().get(1).asInt());
        } else if (value.isNumber()) {
            return new IntegerOption(value.asInt(), name);
        } else {
            return null;
        }
    }
}
