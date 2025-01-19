package config.distribution;

import config.helper.ConfigurationOption;
import config.helper.DoubleOption;
import config.helper.DoubleRangeOption;

import java.util.*;

public abstract class DistributionOption<Enum> implements IDistributionOption<Enum> {

    protected List<Enum> values;
    protected List<Integer> currentThresholds;
    protected List<DoubleOption> staticParameters;
    protected List<DoubleRangeOption> dynamicParameters;
    protected String optionName;
    protected boolean staticDistribution; // Distribution stays the same for every feature model

    public DistributionOption(String name, Map<Enum, ConfigurationOption<Double>> distribution) {
        this.optionName = name;
        this.staticDistribution = true;
        this.staticParameters = new ArrayList<>();
        this.dynamicParameters = new ArrayList<>();
        this.values = new ArrayList<>();
        Set<Enum> keySet = new TreeSet<>(distribution.keySet()); // Ensure keyset order stays the same
        for (Enum value : keySet) { // Ensure static parameters are first
            ConfigurationOption<Double> currentParameter = distribution.get(value);
            if (currentParameter instanceof DoubleOption) {
                staticParameters.add((DoubleOption) currentParameter);
                values.add(value);
            }
        }

        for (Enum value : keySet) {
            ConfigurationOption<Double> currentParameter = distribution.get(value);
            if (currentParameter instanceof DoubleRangeOption) {
                dynamicParameters.add((DoubleRangeOption) currentParameter);
                this.staticDistribution = false;
                values.add(value);
            }
        }


        if (staticDistribution) {
            int latestThreshold = 0;
            this.currentThresholds = new ArrayList<>();
            for (DoubleOption value : staticParameters) {
                int current = (int) (value.getStaticValue() * 1000);
                latestThreshold =  latestThreshold + current;
                currentThresholds.add(latestThreshold);
            }
        }
    }

    @Override
    public void initValue(Random random) {
        if (staticDistribution) {
            return;
        }
        int remaining = 1000;
        List<Integer> distribution = new ArrayList<>();
        int nextIndex = 0;
        for (DoubleOption value : staticParameters) {
            int current = (int) (value.getStaticValue() * 1000);
            distribution.add(current);
            nextIndex++;
        }
        int startDynamic = nextIndex;
        for (DoubleRangeOption value : dynamicParameters) {
            int minimum = (int) (value.getLower() * 1000);
            remaining -= minimum; //
            nextIndex++;
        }
        if (remaining < 0) {
            System.err.println("Ill formed distribution: " + optionName);
        }
        nextIndex = startDynamic;
        for (DoubleRangeOption value : dynamicParameters) {
            int maxDistro = (int) ((value.getUpper() - value.getLower()) * 1000);
            int max = Math.min(maxDistro, remaining); // throw the dice while ensuring that we neither have a distribution higher than 1.0 nor passing the limit of the parameter

            int toAdd = max > 0 ? random.nextInt(0, max) + (int) (value.getLower() * 1000) : 0; // random.nextInt(0,0) is invalid :(
            remaining -= (toAdd - (int) (value.getLower() * 1000));
            distribution.add(nextIndex++, toAdd);
        }
        this.currentThresholds = new ArrayList<>();
        int latestThreshold = 0;
        for (int singleValue : distribution) {
            latestThreshold += singleValue;
            this.currentThresholds.add(latestThreshold);
        }
    }

    @Override
    public Enum getNextValue(Random random) {
        int result = random.nextInt(1,1001);
        for (int i = 0; i < currentThresholds.size(); i++) {
            if (result <= currentThresholds.get(i)) {
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
