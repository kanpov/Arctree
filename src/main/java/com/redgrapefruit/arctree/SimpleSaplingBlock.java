package com.redgrapefruit.arctree;

import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * A default {@code SaplingBlock} implementation that exposes the {@code protected} constructor as {@code public}.
 */
public class SimpleSaplingBlock extends SaplingBlock {
    public SimpleSaplingBlock(@NotNull SaplingGenerator generator, @NotNull Settings settings) {
        super(generator, settings);
    }
}
