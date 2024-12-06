package config;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import de.vill.model.Attribute;
import de.vill.model.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AttributeBundle {

    public List<AttributeOption> attributeOptionList;

    public AttributeBundle(List<AttributeOption> attributeOptionList) {
        this.attributeOptionList = attributeOptionList;
    }

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

    public static AttributeBundle createRandomAttributeBundle(Random rng) {
        List<AttributeOption> attributeList = new ArrayList<>();
        int numberOfAttributes = rng.nextInt(10); // TODO: Random threshold check if this is fine, should never be too many attributes
        for (int i = 0; i < numberOfAttributes; i++) {
            String attributeName = "Attribute" + i;
            int min = 0;
            int max = rng.nextInt(10000); // TODO: random threshold should be large enough to reflect relative differences between attribute values
            double probability = (double) rng.nextInt(100) / 100;
            boolean useInConstraints = true;
            attributeList.add(new AttributeOption(attributeName, min, max, probability, useInConstraints));
        }
        return new AttributeBundle(attributeList);
    }

    public static AttributeBundle fromJson(JsonArray attributes) {
        List<AttributeOption> attributeList = new ArrayList<>();
        for (JsonValue attribute : attributes) {
            JsonObject attributeObject = attribute.asObject();
            String attributeName = attributeObject.get("name").asString();
            int min = attributeObject.get("value").asArray().get(0).asInt();
            int max = attributeObject.get("value").asArray().get(1).asInt();
            double probability = attributeObject.get("attachProbability").asDouble();
            boolean useInConstraints = attributeObject.get("useInConstraints").asBoolean();
            attributeList.add(new AttributeOption(attributeName, min, max, probability, useInConstraints));
        }
        return new AttributeBundle(attributeList);
    }

}
