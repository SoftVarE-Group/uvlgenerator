package config.constraints;

public enum Aggregate {
    SUM("Equals"),
    AVERAGE("Lesser");

    private final String name;

    private Aggregate(String name) {
        this.name = name;
    }

    public String getName() {return name;}

    public static Aggregate fromString(final String name) {
        for (final Aggregate operation : Aggregate.values()) {
            if (operation.name.equalsIgnoreCase(name)) {
                return operation;
            }
        }
        return null;
    }

}
