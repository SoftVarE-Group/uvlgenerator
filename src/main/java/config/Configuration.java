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
    public FeatureCardinalityOption featureCardinality;


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
        this.numberOfFeatures = IntegerOption.parseIntegerOption("numberFeatures", features.get("number"));
        this.featureType = FeatureTypeOption.fromJson(features.get("distribution").asObject());
        this.numberOfChildren = IntegerOption.parseIntegerOption("numberChildren", features.get("number"));
        this.featureCardinality = FeatureCardinalityOption.fromJson(features.get("cardinality").asObject());
        // -- Depth
        this.treeDepth = IntegerOption.parseIntegerOption("treeDepth", tree.get("maxTreeDepth"));
        // -- Groups
        this.groupType = GroupTypeOption.fromJson(tree.get("groups").asObject().get("distribution").asObject());

        // Constraints
        JsonObject constraints = json.get("constraints").asObject();
        this.numberOfConstraints = IntegerOption.parseIntegerOption("numberOfConstraints", constraints.get("number"));
        this.ecr = DoubleOption.parseDoubleOption("ecr", constraints.get("ecr"));
        this.constraintSize = IntegerOption.parseIntegerOption("constraintSize", constraints.get("variablesPerConstraint"));
        this.constraintDistribution = ConstraintTypeOption.fromJson(constraints.get("distribution").asObject());

        // Attributes
        this.attributes = AttributeBundle.fromJson(json.get("attributes").asArray());

    }





}
