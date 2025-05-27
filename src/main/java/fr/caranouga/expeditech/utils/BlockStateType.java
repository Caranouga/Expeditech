package fr.caranouga.expeditech.utils;

public enum BlockStateType {
    CUBE_ALL,
    MACHINE_BLOCK,
    PIPE_BLOCK,
    SKIP(true)
    ;

    private final boolean skip;

    BlockStateType(boolean skip) {
        this.skip = skip;
    }

    BlockStateType() {
        this(false);
    }

    public boolean shouldSkip() {
        return skip;
    }
}
