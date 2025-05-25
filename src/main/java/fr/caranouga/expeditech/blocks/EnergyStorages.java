package fr.caranouga.expeditech.blocks;

import fr.caranouga.expeditech.capability.CustomEnergyStorage;

public enum EnergyStorages {
    TEST_MACHINE(1000, 100, 0),

    COAL_GENERATOR(5000, 0, 100),
    IRON_PIPE(200, 100, 100),
    ;

    private final int capacity;
    private final int maxReceive;
    private final int maxExtract;

    EnergyStorages(int capacity, int maxReceive, int maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public CustomEnergyStorage createEnergyStorage() {
        return new CustomEnergyStorage(capacity, maxReceive, maxExtract);
    }

    public int getCapacity() {
        return capacity;
    }
}
