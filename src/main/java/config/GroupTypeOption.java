package config;

import com.eclipsesource.json.JsonObject;
import de.vill.model.FeatureType;
import de.vill.model.Group;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GroupTypeOption extends DistributionOption<Group.GroupType>{

    public static GroupTypeOption fromJson(JsonObject distribution) {
        Map<Group.GroupType, Double> options = new HashMap<>();
        options.put(Group.GroupType.OPTIONAL, distribution.get("optional").asDouble());
        options.put(Group.GroupType.OR, distribution.get("or").asDouble());
        options.put(Group.GroupType.MANDATORY, distribution.get("mandatory").asDouble());
        options.put(Group.GroupType.ALTERNATIVE, distribution.get("alternative").asDouble());
        options.put(Group.GroupType.GROUP_CARDINALITY, distribution.get("groupCardinality").asDouble());
        return new GroupTypeOption(options);
    }

    public GroupTypeOption(Map<Group.GroupType, Double> distribution){
        super("groupType", distribution);
    }

    @Override
    public Group.GroupType fromString(String value) {
        return Group.GroupType.valueOf(value.toUpperCase());
    }
}
