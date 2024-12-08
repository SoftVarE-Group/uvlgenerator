package config.distribution;

import config.ConfigurationOption;

public interface IDistributionOption<Enum> extends ConfigurationOption<Enum> {
    public Enum fromString(String value);
}
