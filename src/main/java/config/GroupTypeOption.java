package config;

import de.vill.model.Group;

import java.util.Map;
import java.util.Random;

public class GroupTypeOption implements ConfigurationOption<Group.GroupType>{

    private final String optionName;
    int optionalMax;
    int mandatoryMax;
    int alternativeMax;
    int orMax;

    public GroupTypeOption(Map<Group.GroupType, Double> distribution){
        this.optionName = "groupType";
        optionalMax = (int) Math.round(distribution.get(Group.GroupType.OPTIONAL) * 1000);
        mandatoryMax = optionalMax + (int) Math.round(distribution.get(Group.GroupType.MANDATORY) * 1000);
        alternativeMax = mandatoryMax + (int) Math.round(distribution.get(Group.GroupType.ALTERNATIVE) * 1000);
        orMax = alternativeMax + (int) Math.round(distribution.get(Group.GroupType.OR) * 1000);
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public Group.GroupType getNextValue(Random random) {
        int result = random.nextInt(1000);
        if(result <= optionalMax){
            return Group.GroupType.OPTIONAL;
        }else if(result <= mandatoryMax){
            return Group.GroupType.MANDATORY;
        } else if(result <= alternativeMax){
            return Group.GroupType.ALTERNATIVE;
        } else if(result <= orMax){
            return Group.GroupType.OR;
        } else {
            return Group.GroupType.GROUP_CARDINALITY;
        }
    }

    @Override
    public Group.GroupType getStaticValue() {
        return null;
    }
}
