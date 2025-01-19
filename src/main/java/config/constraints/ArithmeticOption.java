package config.constraints;

import com.eclipsesource.json.JsonObject;
import config.helper.ConfigurationOption;
import config.helper.DoubleOption;
import config.helper.DoubleRangeOption;
import config.distribution.DistributionOption;

import java.util.HashMap;
import java.util.Map;

public class ArithmeticOption extends DistributionOption<ArithmeticOperation> {

    private static final String name = "arithmeticOption";

    public ArithmeticOption(Map<ArithmeticOperation, ConfigurationOption<Double>> valueMap) {
        super(name, valueMap);
    }

    public static ArithmeticOption createDefault() {
        Map<ArithmeticOperation, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(ArithmeticOperation.ADD, new DoubleRangeOption("add",  0.2,0.3));
        options.put(ArithmeticOperation.SUBTRACT, new DoubleRangeOption("subtract", 0.2, 0.3));
        options.put(ArithmeticOperation.MULTIPLY, new DoubleRangeOption("multiply", 0.2, 0.3));
        options.put(ArithmeticOperation.DIVIDE, new DoubleRangeOption("divide", 0.2, 0.3));
        return new ArithmeticOption(options);
    }

    public static ArithmeticOption fromJson(JsonObject distribution) {
        if (distribution.isNull() || distribution.isEmpty()) {
            return createDefault();
        }
        Map<ArithmeticOperation, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(ArithmeticOperation.ADD, DoubleOption.parseDoubleOptionJson("add", distribution.get("add"), new double[] {0.2,0.3}));
        options.put(ArithmeticOperation.SUBTRACT, DoubleOption.parseDoubleOptionJson("subtract", distribution.get("subtract"), new double[] {0.2,0.3}));
        options.put(ArithmeticOperation.MULTIPLY, DoubleOption.parseDoubleOptionJson("multiply", distribution.get("multiply"), new double[] {0.2,0.3}));
        options.put(ArithmeticOperation.DIVIDE, DoubleOption.parseDoubleOptionJson("divide", distribution.get("divide"), new double[] {0.2,0.3}));
        return new ArithmeticOption(options);
    }

    @Override
    public ArithmeticOperation fromString(String value) {
        return null;
    }
}
