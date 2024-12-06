package config;

import java.util.Random;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

public class Configuration {

    // General
    public ConfigurationOption<Integer> numberOfModels;
    public Random randomGenerator;
    public BooleanOption ensureSAT;
    // Tree
    public ConfigurationOption<Integer> numberOfFeatures;
    public final static int[] DEFAULT_NO_FEATURES = new int[] {1, 5000};
    public ConfigurationOption<Integer> treeDepth;
    public final static int[] DEFAULT_TREE_DEPTH = new int[] {2, 20};
    public ConfigurationOption<Integer> numberOfChildren;
    public final static int[] DEFAULT_NO_CHILDREN = new int[] {1,100};
    public FeatureTypeOption featureType;
    public FeatureCardinalityOption featureCardinality;


    // Constraints
    public ConfigurationOption<Integer> numberOfConstraints;
    public final static int[] DEFAULT_NO_CONSTRAINTS = new int[] {1, 20000};
    public ConstraintTypeOption constraintDistribution;
    public ConfigurationOption<Integer> constraintSize;
    public final static int[] DEFAULT_CONSTRAINT_SIZE = new int[] {1, 50};
    public ConfigurationOption<Double> ecr;
    public final static double[] DEFAULT_ECR = new double[] {0.01,1.0};
    // Attributes
    public AttributeBundle attributes;

    public GroupTypeOption groupType;

    public void initializeRandom(int models, int seed) {
        this.numberOfModels = new IntegerOption(models, "numberModels");
        this.randomGenerator = new Random(seed);
        this.ensureSAT = new BooleanOption("ensureSAT", true);

        this.numberOfFeatures = IntegerOption.parseIntegerOption("numberFeatures", DEFAULT_NO_FEATURES);
        this.featureType = FeatureTypeOption.createDefault();
        this.numberOfChildren = IntegerOption.parseIntegerOption("numberChildren", DEFAULT_NO_CHILDREN);
        this.featureCardinality = FeatureCardinalityOption.createDefault(this.randomGenerator);

        this.treeDepth = IntegerOption.parseIntegerOption("treeDepth", DEFAULT_TREE_DEPTH);

        this.groupType = GroupTypeOption.createDefault();

        this.numberOfConstraints = IntegerOption.parseIntegerOption("numberConstraints", DEFAULT_NO_CONSTRAINTS);
        this.ecr = DoubleOption.parseDoubleOption("ecr", DEFAULT_ECR);
        this.constraintSize = IntegerOption.parseIntegerOption("constraintSize", DEFAULT_CONSTRAINT_SIZE);
        this.constraintDistribution = ConstraintTypeOption.createDefault();
        this.attributes = AttributeBundle.createRandomAttributeBundle(randomGenerator);
    }

    public void initializeWithJson(String configJson) {
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
        this.numberOfFeatures = IntegerOption.parseIntegerOptionJson("numberFeatures", features.get("number"), DEFAULT_NO_FEATURES);
        this.featureType = FeatureTypeOption.fromJson(features.get("distribution").asObject());
        this.numberOfChildren = IntegerOption.parseIntegerOptionJson("numberChildren", features.get("number"), DEFAULT_NO_CHILDREN);
        this.featureCardinality = FeatureCardinalityOption.fromJson(features.get("cardinality").asObject());
        // -- Depth
        this.treeDepth = IntegerOption.parseIntegerOptionJson("treeDepth", tree.get("maxTreeDepth"), DEFAULT_TREE_DEPTH);
        // -- Groups
        this.groupType = GroupTypeOption.fromJson(tree.get("groups").asObject().get("distribution").asObject());

        // Constraints
        JsonObject constraints = json.get("constraints").asObject();
        this.numberOfConstraints = IntegerOption.parseIntegerOptionJson("numberOfConstraints", constraints.get("number"), DEFAULT_NO_CONSTRAINTS);
        this.ecr = DoubleOption.parseDoubleOptionJson("ecr", constraints.get("ecr"), DEFAULT_ECR);
        this.constraintSize = IntegerOption.parseIntegerOptionJson("constraintSize", constraints.get("variablesPerConstraint"), DEFAULT_CONSTRAINT_SIZE);
        this.constraintDistribution = ConstraintTypeOption.fromJson(constraints.get("distribution").asObject());

        // Attributes
        this.attributes = AttributeBundle.fromJson(json.get("attributes").asArray());

    }





}
