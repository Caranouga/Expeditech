package fr.caranouga.expeditech.utils;

import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class VoxelUtils {
    public static VoxelShape combine(VoxelShape... shapes) {
        VoxelShape combined = VoxelShapes.empty();
        for (VoxelShape shape : shapes) {
            combined = VoxelShapes.join(combined, shape, IBooleanFunction.OR);
        }
        return combined;
    }
}
