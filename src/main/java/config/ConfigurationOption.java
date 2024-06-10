package config;

import java.util.Random;

public interface ConfigurationOption<T> {
    public String getOptionName();

    public T getNextValue(Random random);

    /**
     * Used for options that do not change during the creation of a feature model
     * @return
     */
    public T getStaticValue();

    /**
     * Initialize a value to be used for the entire feature model
     * @param random rng
     */
    default public void initValue(Random random) {}
}
