package config.constraints;

import com.eclipsesource.json.JsonObject;
import config.helper.ConfigurationOption;
import config.helper.DoubleOption;
import config.helper.DoubleRangeOption;
import config.distribution.DistributionOption;

import java.util.HashMap;
import java.util.Map;

public class AggregateOption extends DistributionOption<Aggregate> {

    private static final String name = "arithmeticOption";

    private static Map<String, double[]> defaultValues;

    static {
        defaultValues = new HashMap<>();
        defaultValues.put("sum", new double[] {0.7, 0.95});
        defaultValues.put("avg", new double[] {0.05, 0.3});
    }

    public AggregateOption(Map<Aggregate, ConfigurationOption<Double>> valueMap) {
        super(name, valueMap);
    }

    public static AggregateOption createDefault() {
        Map<Aggregate, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(Aggregate.SUM, new DoubleRangeOption("sum",  defaultValues.get("sum")[0],defaultValues.get("sum")[1]));
        options.put(Aggregate.AVERAGE, new DoubleRangeOption("avg", 0.05, 0.3));
        return new AggregateOption(options);
    }

    public static AggregateOption fromJson(JsonObject distribution) {
        if (distribution.isNull() || distribution.isEmpty()) {
            return createDefault();
        }
        Map<Aggregate, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(Aggregate.SUM, DoubleOption.parseDoubleOptionJson("sum", distribution.get("sum"), defaultValues.get("sum")));
        options.put(Aggregate.AVERAGE, DoubleOption.parseDoubleOptionJson("avg", distribution.get("avg"), defaultValues.get("avg")));
        return new AggregateOption(options);
    }

    @Override
    public Aggregate fromString(String value) {
        return null;
    }

}
