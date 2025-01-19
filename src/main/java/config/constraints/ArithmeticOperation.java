package config.constraints;

public enum ArithmeticOperation {

    ADD("Add"),
    SUBTRACT("Subtract"),
    MULTIPLY("Multiply"),
    DIVIDE("Divide");

    private final String name;

    private ArithmeticOperation(String name) {
        this.name = name;
    }

    public String getName() {return name;}

    public static ArithmeticOperation fromString(final String name) {
        for (final ArithmeticOperation operation : ArithmeticOperation.values()) {
            if (operation.name.equalsIgnoreCase(name)) {
                return operation;
            }
        }
        return null;
    }

}
