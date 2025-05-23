package fr.caranouga.expeditech.blocks.pipes;

public enum PipeType {
    ENERGY("energy_pipe");

    private final String name;

    PipeType(String name) {
        this.name = name;
    }

    public String getName(String blockName) {
        return blockName + "_" + name;
    }
}
