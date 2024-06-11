package config;

public interface IDistributionOption<Enum> extends ConfigurationOption<Enum> {
    public Enum fromString(String value);
}
