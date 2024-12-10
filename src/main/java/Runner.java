import cli.CLIMap;
import config.Configuration;
import de.vill.model.FeatureModel;
import generator.FeatureModelGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Runner {

    public static void main(String[] args) throws IOException {
        CLIMap cli = new CLIMap();
        cli.parseAndVerifyArgs(args);
        Configuration config = new Configuration();

        if (cli.useDefault) {
            config.initializeRandom(cli.numberOfModels, cli.seed);
        } else {
            config.initializeWithJson(Files.readString(Path.of(cli.filePath)));
        }

        FeatureModelGenerator generator = new FeatureModelGenerator();
        List<FeatureModel> result = generator.run(config);
        int index = 0;
        for (FeatureModel featureModel : result) {
            Files.writeString(Path.of("fm" + index++ + ".uvl"), featureModel.toString());
        }
    }

}
