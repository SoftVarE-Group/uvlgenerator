package config.constraints;

import com.eclipsesource.json.JsonObject;
import config.helper.ConfigurationOption;
import config.helper.DoubleOption;
import config.helper.DoubleRangeOption;
import config.distribution.DistributionOption;

import java.util.HashMap;
import java.util.Map;

public class EquationOption extends DistributionOption<Equation> {

    private static final String name = "arithmeticOption";

    public EquationOption(Map<Equation, ConfigurationOption<Double>> valueMap) {
        super(name, valueMap);
    }

    public static EquationOption createDefault() {
        Map<Equation, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(Equation.EQUALS, new DoubleRangeOption("equals",  0.2,0.3));
        options.put(Equation.GREATER, new DoubleRangeOption("greater", 0.2, 0.3));
        options.put(Equation.LESSER, new DoubleRangeOption("lesser", 0.2, 0.3));
        return new EquationOption(options);
    }

    public static EquationOption fromJson(JsonObject distribution) {
        if (distribution.isNull() || distribution.isEmpty()) {
            return createDefault();
        }
        Map<Equation, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(Equation.EQUALS, DoubleOption.parseDoubleOptionJson("equals", distribution.get("equals"), new double[] {0.2,0.3}));
        options.put(Equation.GREATER, DoubleOption.parseDoubleOptionJson("greater", distribution.get("greater"), new double[] {0.2,0.3}));
        options.put(Equation.LESSER, DoubleOption.parseDoubleOptionJson("lesser", distribution.get("lesser"), new double[] {0.2,0.3}));
        return new EquationOption(options);
    }

    @Override
    public Equation fromString(String value) {
        return null;
    }
}
