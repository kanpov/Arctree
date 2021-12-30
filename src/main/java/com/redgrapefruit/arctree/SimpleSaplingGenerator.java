package com.redgrapefruit.arctree;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * A simple {@code SaplingGenerator} implementation that returns the tree {@code ConfiguredFeature} that you passed it.
 */
public class SimpleSaplingGenerator extends SaplingGenerator {
    private final @NotNull ConfiguredFeature<TreeFeatureConfig, ?> feature;

    /**
     * Creates a {@code SimpleSaplingGenerator}
     * @param feature The tree {@code ConfiguredFeature}
     */
    @SuppressWarnings("unchecked")
    public SimpleSaplingGenerator(@NotNull ConfiguredFeature<?, ?> feature) {
        this.feature = (ConfiguredFeature<TreeFeatureConfig, ?>) feature;
    }

    @Nullable
    @Override
    protected ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bees) {
        return feature;
    }
}
