package config;

import java.util.Random;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class Configuration {

    // General
    public ConfigurationOption<Integer> numberOfModels;
    public Random randomGenerator;
    public BooleanOption ensureSAT;
    // Tree
    public ConfigurationOption<Integer> numberOfFeatures;
    public ConfigurationOption<Integer> treeDepth;
    public ConfigurationOption<Integer> numberOfChildren;
    public FeatureTypeOption featureType;


    // Constraints
    public ConfigurationOption<Integer> numberOfConstraints;
    public ConstraintTypeOption constraintDistribution;
    public ConfigurationOption<Integer> constraintSize;
    public ConfigurationOption<Double> ecr;
    // Attributes
    public AttributeBundle attributes;

    public GroupTypeOption groupType;



    public void initialize(String configJson) {
        JsonObject json = Json.parse(configJson).asObject();

        // General
        JsonObject general = json.get("general").asObject();
        this.numberOfModels = new IntegerOption(general.get("numberModels").asInt(), "numberModels");
        this.randomGenerator = new Random(general.get("seed").asLong());
        this.ensureSAT = new BooleanOption("ensureSAT", general.get("ensureSAT").asBoolean());

        // Tree
        JsonObject tree = json.get("tree").asObject();

        // -- Features
        JsonObject features = tree.get("features").asObject();
        this.numberOfFeatures = parseIntegerOption("numberFeatures", features.get("number"));
        this.featureType = FeatureTypeOption.fromJson(features.get("distribution").asObject());
        // -- Depth
        this.treeDepth = parseIntegerOption("treeDepth", tree.get("maxTreedDepth"));
        // -- Groups
        this.groupType = GroupTypeOption.fromJson(features.get("groups").asObject());

        // Constraints
        JsonObject constraints = json.get("constraints").asObject();
        this.numberOfConstraints = parseIntegerOption("numberOfConstraints", constraints.get("number"));
        this.ecr = parseDoubleOption("ecr", constraints.get("ecr"));
        this.constraintSize = parseIntegerOption("constraintSize", constraints.get("variablesPerConstraint"));
        this.constraintDistribution = ConstraintTypeOption.fromJson(constraints.get("distribution").asObject());

        // Attributes
        this.attributes = AttributeBundle.fromJson(json.get("attributes").asArray());

    }

    private ConfigurationOption<Integer> parseIntegerOption(String name, JsonValue value) {
        if (value.isArray()) {
            return new IntegerRangeOption(name, value.asArray().get(0).asInt(), value.asArray().get(1).asInt());
        } else if (value.isNumber()) {
            return new IntegerOption(value.asInt(), name);
        } else {
            return null;
        }
    }

    private ConfigurationOption<Double> parseDoubleOption(String name, JsonValue value) {
        if (value.isArray()) {
            return new DoubleRangeOption(name, value.asArray().get(0).asDouble(), value.asArray().get(1).asDouble());
        } else if (value.isNumber()) {
            return new DoubleOption(name, value.asDouble());
        } else {
            return null;
        }
    }

}
