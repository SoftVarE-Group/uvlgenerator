package config;

import java.util.Random;

public class Configuration {

    public ConfigurationOption<Integer> numberOfFeatures;
    public ConfigurationOption<Integer> numberOfConstraints;
    public ConfigurationOption<Integer> treeDepth;
    public ConfigurationOption<Integer> numberOfChildren;
    public Random randomGenerator;
    public GroupTypeOption groupType;
    public AttributeBundle attributes;


    public void initialize(String configJson) {
        // TODO
    }
}
