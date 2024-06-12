import config.Configuration;
import de.vill.model.FeatureModel;
import generator.FeatureModelGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws IOException {
        Configuration config = new Configuration();
        config.initialize(Files.readString(Path.of("/home/chico/git/uvl/uvlgenerator/input_examples/full.json")));
        FeatureModelGenerator generator = new FeatureModelGenerator();
        List<FeatureModel> result = generator.run(config);
        int index = 0;
        for (FeatureModel featureModel : result) {
            Files.writeString(Path.of("/home/chico/git/uvl/uvlgenerator/fm" + index++ + ".uvl"), featureModel.toString());
        }
    }

}
