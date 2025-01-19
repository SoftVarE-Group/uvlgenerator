package config.distribution;

import config.helper.ConfigurationOption;

public interface IDistributionOption<Enum> extends ConfigurationOption<Enum> {
    public Enum fromString(String value);
}
