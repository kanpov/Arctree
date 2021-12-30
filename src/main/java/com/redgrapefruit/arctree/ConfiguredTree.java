package com.redgrapefruit.arctree;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Represents the output of an {@link TreeBuilder} instance.
 * <br><br>
 * The {@link ConfiguredTree} supports <b>automatic registering</b> for your tree, which is the preferred
 * way of doing it.
 * <br><br>
 * In Minecraft 1.18, you need to register {@link ConfiguredFeature}s <b>and</b> {@link PlacedFeature}s for your tree,
 * while in 1.17 and earlier you only had a {@link PlacedFeature}. Thus, the result of {@link TreeBuilder#build()}
 * can no longer be a single variable.
 * <br><br>
 * {@link PlacedFeature}s essentially replace the Decorator system that 1.17 and earlier had with flexible and
 * easy-to-use {@link PlacementModifier}s.
 */
public final class ConfiguredTree {
    private final @NotNull ConfiguredFeature<?, ?> configuredFeature;
    private final @NotNull PlacedFeature placedFeature;
    private final @NotNull Predicate<BiomeSelectionContext> selector;

    public ConfiguredTree(
            @NotNull ConfiguredFeature<?, ?> configuredFeature,
            @NotNull PlacedFeature placedFeature,
            @NotNull Predicate<BiomeSelectionContext> selector) {

        Objects.requireNonNull(configuredFeature);
        Objects.requireNonNull(placedFeature);

        this.configuredFeature = configuredFeature;
        this.placedFeature = placedFeature;
        this.selector = selector;
    }

    public @NotNull ConfiguredFeature<?, ?> getConfiguredFeature() {
        return configuredFeature;
    }

    public @NotNull PlacedFeature getPlacedFeature() {
        return placedFeature;
    }

    public @NotNull Pair<ConfiguredFeature<?, ?>, PlacedFeature> asPair() {
        return new Pair<>(configuredFeature, placedFeature);
    }

    public @NotNull Predicate<BiomeSelectionContext> getBiomeSelector() {
        return selector;
    }

    /**
     * Automatically registers everything needed for your tree.
     *
     * @param id The {@link Identifier}, under which your tree should be registered.
     */
    public void register(@NotNull Identifier id) {
        Objects.requireNonNull(id, "ID must not be null");

        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
        Registry.register(BuiltinRegistries.PLACED_FEATURE, id, placedFeature);
        BiomeModifications.addFeature(selector, GenerationStep.Feature.VEGETAL_DECORATION, RegistryKey.of(Registry.PLACED_FEATURE_KEY, id));
    }
}
