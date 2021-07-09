package com.redgrapefruit.arctree.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// Suppress warnings when a {@link} reference a private method since it's invoked by the mixin
@SuppressWarnings("JavadocReference")
@Mixin(TrunkPlacerType.class)
public interface TrunkPlacerTypeInvoker {
    /**
     * Invokes the {@link TrunkPlacerType#register} method that creates a new {@link TrunkPlacerType}, registers it and returns it
     */
    @Invoker
    @NotNull
    static <T extends TrunkPlacer> TrunkPlacerType<T> invokeRegister(@NotNull String id, @NotNull Codec<T> codec) {
        throw new AssertionError();
    }
}
