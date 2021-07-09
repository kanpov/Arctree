package com.redgrapefruit.arctree.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.BlockStateProviderType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// Suppress warnings when a {@link} reference a private method
@SuppressWarnings("JavadocReference")
@Mixin(BlockStateProviderType.class)
public interface BlockStateProviderTypeInvoker {
    /**
     * Invokes the {@link BlockStateProviderType#register} method that creates a new {@link BlockStateProvider}, registers it and returns it
     */
    @Invoker
    @NotNull
    static <T extends BlockStateProvider> BlockStateProviderType<T> invokeRegister(@NotNull String id, @NotNull Codec<T> codec) {
        throw new AssertionError();
    }
}
