package fr.caranouga.expeditech.blocks;

public enum PipeTypes {
    ENERGY("energy"),
    ;

    private final String name;

    PipeTypes(String name) {
        this.name = name;
    }

    public String getName(String prefix) {
        return prefix + "_" + name;
    }
}
