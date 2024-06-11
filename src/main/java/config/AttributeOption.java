package config;

import de.vill.model.Attribute;
import de.vill.model.Feature;

import java.util.Random;

public class AttributeOption {
    public final String attributeName;
    public final int min;
    public final int max;
    public final double probability;
    public final boolean includeInConstraints;

    public AttributeOption(String attributeName, int min, int max, double probability, boolean includeInConstraints) {
        this.attributeName = attributeName;
        this.min = min;
        this.max = max;
        this.probability = probability;
        this.includeInConstraints = includeInConstraints;
    }


    public Attribute<Integer> getNextValue(Random random, Feature parent) {
        if (random.nextInt(1000) > probability * 1000) {return null;}
        return new Attribute<Integer>(attributeName, random.nextInt(max - min) + min, parent);

    }

}
