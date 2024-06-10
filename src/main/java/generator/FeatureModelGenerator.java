package generator;

import config.Configuration;
import de.vill.model.*;
import de.vill.model.building.FeatureModelBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeatureModelGenerator {
    FeatureModelBuilder builder;
    Configuration config;
    int currentId;

    List<Group> parentGroups;

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

    private void constructConstraints() {

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

}
