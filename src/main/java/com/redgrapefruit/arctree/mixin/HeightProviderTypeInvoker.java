package com.redgrapefruit.arctree.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.heightprovider.HeightProviderType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// Suppress warnings when a {@link} reference a private method
@Mixin(HeightProviderType.class)
@SuppressWarnings("JavadocReference")
public interface HeightProviderTypeInvoker {
    /**
     * Invokes the {@link HeightProviderType#register(String, Codec)} method and returns its {@link HeightProviderType} result (and registers it).
     */
    @Invoker
    @NotNull
    static <T extends HeightProvider> HeightProviderType<T> invokeRegister(@NotNull String id, @NotNull Codec<T> codec) {
        throw new AssertionError();
    }
}
