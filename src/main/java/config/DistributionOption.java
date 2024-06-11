package config;

import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class DistributionOption<Enum> implements IDistributionOption<Enum> {

    protected List<Enum> values;
    protected List<Integer> thresholds;
    protected String optionName;

    public DistributionOption(String name, Map<Enum, Double> distribution) {
        this.optionName = name;
        int latestThreshold = 0;
        for (Enum value : distribution.keySet()) {
            latestThreshold = (int) Math.round(distribution.get(value) * 1000) + latestThreshold;
            thresholds.add(latestThreshold);
            values.add(value);
        }
    }

    @Override
    public Enum getNextValue(Random random) {
        int result = random.nextInt(1000);
        for (int i = 0; i < thresholds.size(); i++) {
            if (result <= thresholds.get(i)) {
                return values.get(i);
            }
        }
        return values.getLast();
    }

    @Override
    public Enum getStaticValue() {
        return null;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }
}
