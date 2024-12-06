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

    public static ConfigurationOption<Integer> parseIntegerOption(String name, int[] values) {
        if (values.length == 1) {
            return new IntegerOption(values[0], name);
        } else if (values.length == 2) {
            return new IntegerRangeOption(name, values[0], values[1]);
        }
        return null;
    }

    public static ConfigurationOption<Integer> parseIntegerOptionJson(String name, JsonValue value, int[] defaultValue) {
        if (value.isArray()) {
            return new IntegerRangeOption(name, value.asArray().get(0).asInt(), value.asArray().get(1).asInt());
        } else if (value.isNumber()) {
            return new IntegerOption(value.asInt(), name);
        }
        return parseIntegerOption(name, defaultValue);
    }
}
