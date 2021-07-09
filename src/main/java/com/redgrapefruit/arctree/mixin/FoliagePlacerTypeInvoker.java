package com.redgrapefruit.arctree.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// Suppress warnings when a {@link} reference a private method
@SuppressWarnings("JavadocReference")
@Mixin(FoliagePlacerType.class)
public interface FoliagePlacerTypeInvoker {
    /**
     * Invokes the {@link FoliagePlacerType#register} method that creates a new {@link FoliagePlacerType}, registers it and returns it
     */
    @Invoker
    @NotNull
    static <T extends FoliagePlacer> FoliagePlacerType<T> invokeRegister(@NotNull String id, @NotNull Codec<T> codec) {
        throw new AssertionError();
    }
}
