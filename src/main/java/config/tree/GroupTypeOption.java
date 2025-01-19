package config.tree;

import com.eclipsesource.json.JsonObject;
import config.helper.ConfigurationOption;
import config.helper.DoubleOption;
import config.helper.DoubleRangeOption;
import config.distribution.DistributionOption;
import de.vill.model.Group;

import java.util.HashMap;
import java.util.Map;

public class GroupTypeOption extends DistributionOption<Group.GroupType> {

    private static final Map<String, double[]> defaultValues;

    static {
        defaultValues = new HashMap<>();
        defaultValues.put("optional", new double[] {0.0, 0.5});
        defaultValues.put("or", new double[] {0.0, 0.5});
        defaultValues.put("mandatory", new double[] {0.0, 0.5});
        defaultValues.put("alternative", new double[] {0.0, 0.5});
        defaultValues.put("groupCardinality", new double[] {0.0, 0.5});

    }


    public static GroupTypeOption createDefault() {
        Map<Group.GroupType, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(Group.GroupType.OPTIONAL, new DoubleRangeOption("optional", defaultValues.get("optional")[0], defaultValues.get("optional")[1]));
        options.put(Group.GroupType.OR, new DoubleRangeOption("or", defaultValues.get("or")[0], defaultValues.get("or")[1]));
        options.put(Group.GroupType.MANDATORY, new DoubleRangeOption("mandatory", defaultValues.get("mandatory")[0], defaultValues.get("mandatory")[1]));
        options.put(Group.GroupType.ALTERNATIVE, new DoubleRangeOption("alternative", defaultValues.get("alternative")[0], defaultValues.get("alternative")[1]));
        options.put(Group.GroupType.GROUP_CARDINALITY, new DoubleRangeOption("groupCardinality", defaultValues.get("groupCardinality")[0], defaultValues.get("groupCardinality")[1]));
        return new GroupTypeOption(options);
    }

    public static GroupTypeOption fromJson(JsonObject distribution) {
        Map<Group.GroupType, ConfigurationOption<Double>> options = new HashMap<>();
        options.put(Group.GroupType.OPTIONAL, DoubleOption.parseDoubleOptionJson("optional", distribution.get("optional"), defaultValues.get("optional")));
        options.put(Group.GroupType.OR, DoubleOption.parseDoubleOptionJson("or", distribution.get("or"), defaultValues.get("or")));
        options.put(Group.GroupType.MANDATORY, DoubleOption.parseDoubleOptionJson("mandatory", distribution.get("mandatory"), defaultValues.get("mandatory")));
        options.put(Group.GroupType.ALTERNATIVE, DoubleOption.parseDoubleOptionJson("alternative", distribution.get("alternative"), defaultValues.get("alternative")));
        options.put(Group.GroupType.GROUP_CARDINALITY, DoubleOption.parseDoubleOptionJson("groupCardinality", distribution.get("groupCardinality"), defaultValues.get("groupCardinality")));
        return new GroupTypeOption(options);
    }

    public GroupTypeOption(Map<Group.GroupType, ConfigurationOption<Double>> distribution){
        super("groupType", distribution);
    }

    @Override
    public Group.GroupType fromString(String value) {
        return Group.GroupType.valueOf(value.toUpperCase());
    }
}
