package config;

public enum Equation {

    EQUALS("Equals"),
    LESSER("Lesser"),
    GREATER("Greater");

    private final String name;

    private Equation(String name) {
        this.name = name;
    }

    public String getName() {return name;}

    public static Equation fromString(final String name) {
        for (final Equation operation : Equation.values()) {
            if (operation.name.equalsIgnoreCase(name)) {
                return operation;
            }
        }
        return null;
    }
}
