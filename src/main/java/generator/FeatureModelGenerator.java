package generator;

import config.AttributeOption;
import config.Configuration;
import config.ConstraintTypeOption;
import de.vill.model.*;
import de.vill.model.building.FeatureModelBuilder;
import de.vill.model.constraint.*;
import de.vill.model.expression.*;

import java.util.*;
import java.util.stream.Collectors;

public class FeatureModelGenerator {
    FeatureModelBuilder builder;
    Configuration config;
    int currentId;
    List<Feature> featuresToUse;
    List<Attribute<?>> attributesToUse;
    List<String> attributeNames;

    List<Group> parentGroups;

    public List<FeatureModel> run(Configuration config) {
        this.config = config;
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
        cleanUpTree();
    }

    private void cleanUpTree() {

    }

    private void constructConstraints() {
        featuresToUse = getFeaturesToUseInConstraints();
        initAttributesToUseInConstraints(featuresToUse);

        while (builder.getFeatureModel().getOwnConstraints().size() < config.numberOfConstraints.getStaticValue()) {
            builder.addConstraint(getNextConstraint());
        }
    }

    private Constraint getNextConstraint() {
        return switch (getNextConstraintType()) {
            case STRING -> generateStringConstraint();
            case AGGREGATE -> generateAggregateConstraint();
            case BOOLEAN -> generateBooleanConstraint();
            default -> genereateNumericConstraint();
        };
    }

    private Constraint generateStringConstraint() {
        int numberOfVariables = config.constraintSize.getNextValue(config.randomGenerator);
        return null;
    }

    private Constraint generateBooleanConstraint() {
        int numberOfVariables = config.constraintSize.getNextValue(config.randomGenerator);
        int[] variableIndices = getRandomIndices(featuresToUse.size(), numberOfVariables, config.randomGenerator);
        List<LiteralConstraint> literals = Arrays.stream(variableIndices).mapToObj(x -> new LiteralConstraint(featuresToUse.get(x))).collect(Collectors.toList());
        Constraint previous = config.randomGenerator.nextInt(2) == 0 ? literals.get(0) : new NotConstraint(literals.get(0));
        for (int i = 1; i < literals.size(); i++) {
            previous = getNextBooleanSubConstraint(literals.get(i), previous);
        }
        return previous;
    }

    private Constraint getNextBooleanSubConstraint(Constraint nextLiteral, Constraint previous) {
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
        int numberOfVariables = config.constraintSize.getNextValue(config.randomGenerator);
        int[] variableIndices = getRandomIndices(attributesToUse.size(), numberOfVariables, config.randomGenerator);
        int rightSideSwap = config.randomGenerator.nextInt(numberOfVariables);
        List<LiteralExpression> literals = Arrays.stream(variableIndices).mapToObj(x -> new LiteralExpression(attributesToUse.get(x))).collect(Collectors.toList());
        Expression previous = literals.get(0);
        for (int i = 1; i < rightSideSwap; i++) {
            previous = getNextNumericSubExpression(literals.get(i), previous);
        }
        Expression leftSide = previous;
        previous = literals.get(rightSideSwap);
        for (int i = rightSideSwap + 1; i < numberOfVariables; i++) {
            previous = getNextNumericSubExpression(literals.get(i), previous);
        }
        switch (config.randomGenerator.nextInt(6)) {
            case 0:
                return new EqualEquationConstraint(leftSide, previous);
            case 1:
                return new GreaterEquationConstraint(leftSide, previous);
            case 2:
                return new GreaterEqualsEquationConstraint(leftSide, previous);
            case 3:
                return new LowerEquationConstraint(leftSide, previous);
            case 4:
                return new LowerEqualsEquationConstraint(leftSide, previous);
            default:
                return new NotEqualsEquationConstraint(leftSide, previous);
        }
    }

    private Expression getNextNumericSubExpression(Expression nextLiteral, Expression previous) {
        switch (config.randomGenerator.nextInt(4)) {
            case 0:
                return new AddExpression(nextLiteral, previous);
            case 1:
                return new SubExpression(nextLiteral, previous);
            case 2:
                return new MulExpression(nextLiteral, previous);
            default:
                return new DivExpression(nextLiteral, previous);
        }
    }

    /**
     * TODO: More flexiblity
     * @return
     */
    private Constraint generateAggregateConstraint() {
        String attributeName = attributeNames.get(config.randomGenerator.nextInt(attributeNames.size()));
        int threshold = 0;
        boolean average = false;
        if (config.randomGenerator.nextInt(2) == 1) {
            average = true;
        }
        for (AttributeOption attributeOption : config.attributes.attributeOptionList) {
            if (attributeOption.attributeName.equals(attributeName)) {
                if (average) {
                    threshold = config.randomGenerator.nextInt(attributeOption.max - attributeOption.min) + attributeOption.min;
                } else {
                    threshold = config.randomGenerator.nextInt(attributeOption.max * 5); // TODO: replace
                }
            }
        }
        if (average) {
            return new GreaterEqualsEquationConstraint(new SumAggregateFunctionExpression(new GlobalAttribute(attributeName, builder.getFeatureModel())), new NumberExpression((double) threshold));
        } else {
            return new GreaterEqualsEquationConstraint(new AvgAggregateFunctionExpression(new GlobalAttribute(attributeName, builder.getFeatureModel())), new NumberExpression((double) threshold));
        }
    }

    private List<Feature> getFeaturesToUseInConstraints() {
        int numberOfFeatures = (int) (config.ecr.getStaticValue() * (double) builder.getFeatureModel().getFeatureMap().size());
        List<Feature> shuffleCopy = new ArrayList<>(builder.getFeatureModel().getFeatureMap().values());
        Collections.shuffle(shuffleCopy);
        return shuffleCopy.subList(0, numberOfFeatures);
    }

    private void initAttributesToUseInConstraints(List<Feature> featuresToUse) {
        attributesToUse = new ArrayList<>();
        attributeNames = config.attributes.attributeOptionList.stream().filter(x -> x.includeInConstraints).map(x -> x.attributeName).collect(Collectors.toList());
        for (String attributeName : attributeNames) {
            for (Feature feature : featuresToUse) {
                if (feature.getAttributes().containsKey(attributeName)) {
                    attributesToUse.add(feature.getAttributes().get(attributeName));
                }
            }
        }
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
        return parentGroups.get(config.randomGenerator.nextInt(parentGroups.size()));
    }

    /**
     * TODO: Wow this is ugly
     * @param length
     * @param numberOf
     * @param random
     * @return
     */
    private static int[] getRandomIndices(int length, int numberOf, Random random) {
        int[] indices = new int[numberOf];
        for (int i = 0; i < numberOf; i++) {
            boolean alreadyCovered;
            int randomIndex;
            do {
                randomIndex = random.nextInt(length);
                alreadyCovered = false;
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
