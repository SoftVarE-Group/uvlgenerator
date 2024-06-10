package config;

import de.vill.model.Attribute;
import de.vill.model.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AttributeBundle {

    List<AttributeOption> attributeOptionList;

    public List<Attribute<?>> getAttributesToAdd(Random random, Feature feature) {
        List<Attribute<?>> attributes = new ArrayList<>();
        for (AttributeOption attributeOption : attributeOptionList) {
            Attribute<Integer> attribute = attributeOption.getNextValue(random, feature);
            if (attribute != null) {
                attributes.add(attribute);
            }
        }
        return attributes;
    }

    public class AttributeOption {
        final String attributeName;
        final int min;
        final int max;
        final int probability;

        public AttributeOption(String attributeName, int min, int max, int probability) {
            this.attributeName = attributeName;
            this.min = min;
            this.max = max;
            this.probability = probability;
        }


        public Attribute<Integer> getNextValue(Random random, Feature parent) {
            if (random.nextInt(1000) > probability * 1000) {return null;}
            return new Attribute<Integer>(attributeName, random.nextInt(max - min) + min, parent);

        }

    }

}
