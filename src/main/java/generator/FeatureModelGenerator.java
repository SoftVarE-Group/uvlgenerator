package generator;

import config.Configuration;
import config.ConstraintTypeOption;
import de.vill.model.*;
import de.vill.model.building.FeatureModelBuilder;
import de.vill.model.constraint.*;

import java.util.*;
import java.util.stream.Collectors;

public class FeatureModelGenerator {
    FeatureModelBuilder builder;
    Configuration config;
    int currentId;
    List<Feature> featuresToUse;
    List<Attribute<?>> attributesToUse;

    List<Group> parentGroups;

    public List<FeatureModel> run(Configuration config) {
        List<FeatureModel> models = new ArrayList<>();
        while (models.size() < config.numberOfModels.getStaticValue()) {
            models.add(nextFeatureModel());
        }
        return models;
    }

    private FeatureModel nextFeatureModel() {
        currentId = 0;
        parentGroups = new ArrayList<>();
        builder = new FeatureModelBuilder();

        constructTree();
        constructConstraints();

        return builder.getFeatureModel();
    }

    private void constructTree() {
        Feature rootFeature = builder.addRootFeature(getNextFeatureName(), getNextFeatureType(), getNextCardinality());
        addGroupToFeature(rootFeature, getNextGroupType());
        while (builder.getFeatureModel().getFeatureMap().size() < config.numberOfFeatures.getStaticValue()) {
            Feature current = builder.addFeature(getNextFeatureName(), getNextGroup(), null, getNextFeatureType(), getNextCardinality());
            for (Attribute<?> attribute : config.attributes.getAttributesToAdd(config.randomGenerator, current)) {
                builder.addAttribute(current, attribute);
            }
            if (getDepthOfFeature(current) < config.treeDepth.getStaticValue()) {
                addGroupToFeature(current, getNextGroupType());
            }
        }
    }

    private void constructConstraints() {
        featuresToUse = getFeaturesToUseInConstraints();
        attributesToUse = getAttributesToUseInConstraints(featuresToUse);

        while (builder.getFeatureModel().getOwnConstraints().size() < config.numberOfConstraints.getStaticValue()) {
            builder.addConstraint(getNextConstraint());
        }
    }

    private Constraint getNextConstraint() {
        switch (getNextConstraintType()) {
            case STRING:
                return generateStringConstraint();
            case AGGREGATE:
                return generateAggregateConstraint();
            case BOOLEAN:
                return generateBooleanConstraint();
            default:
                return genereateNumericConstraint();
        }
    }

    private Constraint generateStringConstraint() {
        int numberOfVariables = config.constraintSize.getNextValue(config.randomGenerator);
    }

    private Constraint generateBooleanConstraint() {
        int numberOfVariables = config.constraintSize.getNextValue(config.randomGenerator);
        int[] variableIndices = getRandomIndices(featuresToUse.size(), numberOfVariables, config.randomGenerator);
        List<LiteralConstraint> literals = Arrays.stream(variableIndices).mapToObj(x -> new LiteralConstraint(featuresToUse.get(x))).collect(Collectors.toList());
        Constraint previous = config.randomGenerator.nextInt(2) == 0 ? literals.get(0) : new NotConstraint(literals.get(0));
        for (int i = 1; i < literals.size(); i++) {
            previous = getNextSubConstraint(literals.get(i), previous);
        }
        return previous;
    }

    private Constraint getNextSubConstraint(Constraint nextLiteral, Constraint previous) {
        switch (config.randomGenerator.nextInt(4)) {
            case 0:
                return new AndConstraint(nextLiteral, previous);
            case 1:
                return new OrConstraint(nextLiteral, previous);
            case 2:
                return new EquivalenceConstraint(nextLiteral, previous);
            default:
                return new ImplicationConstraint(nextLiteral, previous);
        }
    }


    private Constraint genereateNumericConstraint() {

    }

    private Constraint generateAggregateConstraint() {

    }

    private List<Feature> getFeaturesToUseInConstraints() {
        int numberOfFeatures = (int) (config.ecr.getStaticValue() * (double) builder.getFeatureModel().getFeatureMap().size());
        List<Feature> shuffleCopy = new ArrayList<>(builder.getFeatureModel().getFeatureMap().values());
        Collections.shuffle(shuffleCopy);
        return shuffleCopy.subList(0, numberOfFeatures);
    }

    private List<Attribute<?>> getAttributesToUseInConstraints(List<Feature> featuresToUse) {
        List<Attribute<?>> attributesToUse = new ArrayList<>();
        List<String> attributeNames = config.attributes.attributeOptionList.stream().filter(x -> x.includeInConstraints).map(x -> x.attributeName).collect(Collectors.toList());
        for (String attributeName : attributeNames) {
            for (Feature feature : featuresToUse) {
                if (feature.getAttributes().containsKey(attributeName)) {
                    attributesToUse.add(feature.getAttributes().get(attributeName));
                }
            }
        }
        return attributesToUse;

    }

    private ConstraintTypeOption.ConstraintType getNextConstraintType() {
        return config.constraintDistribution.getNextValue(config.randomGenerator);
    }

    private void addGroupToFeature(Feature feature, Group.GroupType groupType) {
        parentGroups.add(builder.addGroup(feature, getNextGroupType()));
    }

    private int getDepthOfFeature(Feature feature) {
        int depth = 0;
        Group group = feature.getParentGroup();
        while (group != null) {
            group = group.getParentFeature().getParentGroup();
            depth++;
        }
        return depth;
    }



    private String getNextFeatureName() {
        return "Feature-" + currentId++;
    }

    private FeatureType getNextFeatureType() {
        return FeatureType.BOOL;
    }

    private Cardinality getNextCardinality() {
        return null;
    }

    private Group.GroupType getNextGroupType() {
        return config.groupType.getNextValue(config.randomGenerator);
    }

    /**
     * Currently just takes random group to add feature
     * @return
     */
    private Group getNextGroup() {
        return parentGroups.get(config.randomGenerator.nextInt(parentGroups.size() - 1));
    }

    /**
     * TODO: Wow this is ugly
     * @param length
     * @param numberOf
     * @param random
     * @return
     */
    private static int[] getRandomIndices(int length, int numberOf, Random random) {
        int[] indices = new int[length];
        for (int i = 0; i < numberOf; i++) {
            boolean alreadyCovered = false;
            int randomIndex;
            do {
                randomIndex = random.nextInt(length);
                for (int alreadyIncluded : indices) {
                    if (randomIndex == alreadyIncluded) {
                        alreadyCovered = true;
                    }
                }
            } while (alreadyCovered);
            indices[i] = randomIndex;
        }
        return indices;
    }

}
