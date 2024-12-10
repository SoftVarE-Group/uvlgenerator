package cli;

import java.util.Arrays;

public class CLIMap {

    public static final String FILE = "--file";

    public static final String BOUNDED_RANDOM = "--boundedRandom";

    public static final String SEED = "--seed";

    public static final String NUMBER_OF_MODELS ="--n";

    public static final String[] AVAILABLE_ARGS = new String[] {FILE, BOUNDED_RANDOM, SEED, NUMBER_OF_MODELS};

    public String filePath = null;

    public int seed = 42;

    public boolean useDefault = false;

    public int numberOfModels = 0;

    public void parseAndVerifyArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case FILE:
                    filePath = args[++i];
                    break;
                case BOUNDED_RANDOM:
                    useDefault = true;
                    break;
                case NUMBER_OF_MODELS:
                    numberOfModels = Integer.parseInt(args[++i]);
                    break;
                case SEED:
                    seed = Integer.parseInt(args[++i]);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal argument: " + arg + ". The following ones are available: " + Arrays.toString(AVAILABLE_ARGS));
            }
        }
        if (!useDefault && filePath == null) {
            throw new IllegalArgumentException("No file or behavior specified. Please use --file <file> or --default.");
        }
        if (useDefault && numberOfModels <= 0) {
            throw new IllegalArgumentException("Number of models must be greater than 0. Please use --n <numberOfModels> when using --default.");
        }
    }

}
