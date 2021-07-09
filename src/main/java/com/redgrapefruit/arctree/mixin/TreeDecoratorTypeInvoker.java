package com.redgrapefruit.arctree.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// Suppress warnings when a {@link} reference a private method
@SuppressWarnings("JavadocReference")
@Mixin(TreeDecoratorType.class)
public interface TreeDecoratorTypeInvoker {
    /**
     * Invokes the {@link TreeDecoratorType#register} method that creates a new {@link TreeDecoratorType}, registers it and returns it
     */
    @Invoker
    @NotNull
    static <T extends TreeDecorator> TreeDecoratorType<T> invokeRegister(@NotNull String id, @NotNull Codec<T> codec) {
        throw new AssertionError();
    }
}
