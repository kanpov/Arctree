package com.redgrapefruit.arctree;

import com.google.common.collect.Lists;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.decorator.HeightmapPlacementModifier;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.decorator.RarityFilterPlacementModifier;
import net.minecraft.world.gen.decorator.SquarePlacementModifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * An easy-to-use builder for tree {@code ConfiguredFeature}s.
 * <br><br>
 * This is technically optional, but highly recommended to use.
 */
public final class TreeBuilder {
    /**
     * The {@code BlockStateProvider} for the tree's trunk
     */
    @Mandatory
    @Nullable
    private BlockStateProvider trunkProvider;

    /**
     * The {@code BlockStateProvider} for the tree's foliage
     */
    @Mandatory
    @Nullable
    private BlockStateProvider foliageProvider;

    /**
     * The {@code TrunkPlacer} for the tree
     */
    @Mandatory
    @Nullable
    private TrunkPlacer trunkPlacer;

    /**
     * The {@code FoliagePlacer} for the tree
     */
    @Mandatory
    @Nullable
    private FoliagePlacer foliagePlacer;

    /**
     * The minimum {@code FeatureSize} for the tree.
     */
    @Optional
    @NotNull
    private FeatureSize minimumSize = new TwoLayersFeatureSize(1, 0, 1);

    /**
     * An {@code ImmutableList} of all {@code TreeDecorator}s used in this tree.
     */
    @Optional
    @NotNull
    private final List<TreeDecorator> decorators = new ArrayList<>();

    /**
     * The {@code BlockStateProvider} for dirt generated underneath the tree.
     */
    @Optional
    @NotNull
    private BlockStateProvider dirtProvider = SimpleBlockStateProvider.of(Blocks.DIRT.getDefaultState());

    /**
     * Does this tree ignore vine generation.
     *
     * {@code false} by default.
     */
    @Optional
    private boolean ignoreVines = false;

    /**
     * Place dirt forcefully.
     */
    @Optional
    private boolean forceDirt = false;

    /**
     * The chance of spawning the tree from 0 to 10.
     * <br><br>
     * <b>Note:</b> this chance is <b>not</b> a percentage! It is a chance of x out of 10, for example, 3
     * represents a chance of 3 out of 10, or 33%. Be careful!
     */
    @Optional
    @EffectiveWithDefaultModifiersOnly
    private int spawnChance = 3;

    /**
     * Custom added {@link PlacementModifier}s.
     */
    @Optional
    private final List<PlacementModifier> addedPlacementModifiers = new ArrayList<>();

    /**
     * Whether to override the default applied {@link PlacementModifier}s.
     * <br><br>
     * If true, only {@link PlacementModifier}s from {@link #addedPlacementModifiers} will be used.
     */
    @Optional
    private boolean overrideDefaultPlacementModifiers = false;

    /**
     * A Fabric Biome Selector that determines where your tree will be found.
     */
    @Optional
    private Predicate<BiomeSelectionContext> biomeSelector = BiomeSelectors.foundInOverworld();

    /**
     * Use {@link #create}
     */
    private TreeBuilder() {}

    /**
     * Creates a new {@link TreeBuilder}
     *
     * @return Created {@link TreeBuilder}
     */
    public static @NotNull TreeBuilder create() {
        return new TreeBuilder();
    }

    @Mandatory
    public @NotNull TreeBuilder trunkPlacer(@NotNull TrunkPlacer trunkPlacer) {
        Objects.requireNonNull(trunkPlacer, "Trunk placer must not be null");

        this.trunkPlacer = trunkPlacer;
        return this;
    }

    @Mandatory
    public @NotNull TreeBuilder foliagePlacer(@NotNull FoliagePlacer foliagePlacer) {
        Objects.requireNonNull(foliagePlacer, "Foliage placer must not be null");

        this.foliagePlacer = foliagePlacer;
        return this;
    }

    @Mandatory
    public @NotNull TreeBuilder trunkProvider(@NotNull BlockStateProvider trunkProvider) {
        Objects.requireNonNull(trunkProvider, "Trunk provider must not be null");

        this.trunkProvider = trunkProvider;
        return this;
    }

    @Mandatory
    public @NotNull TreeBuilder foliageProvider(@NotNull BlockStateProvider foliageProvider) {
        Objects.requireNonNull(foliageProvider, "Foliage provider must not be null");

        this.foliageProvider = foliageProvider;
        return this;
    }

    @Optional
    public @NotNull TreeBuilder minimumSize(@NotNull FeatureSize minimumSize) {
        Objects.requireNonNull(minimumSize, "Feature size must not be null");

        this.minimumSize = minimumSize;
        return this;
    }

    @Optional
    public @NotNull TreeBuilder addDecorator(@NotNull TreeDecorator decorator) {
        Objects.requireNonNull(decorator, "Tree decorator must not be null");

        this.decorators.add(decorator);
        return this;
    }

    @Optional
    public @NotNull TreeBuilder dirtProvider(@NotNull BlockStateProvider dirtProvider) {
        Objects.requireNonNull(dirtProvider, "Dirt provider must not be null");

        this.dirtProvider = dirtProvider;
        return this;
    }

    @Optional
    public @NotNull TreeBuilder ignoreVines() {
        this.ignoreVines = true;
        return this;
    }

    @Optional
    public @NotNull TreeBuilder forceDirt() {
        this.forceDirt = true;
        return this;
    }

    @Optional
    public @NotNull TreeBuilder spawnChance(int spawnChance) {
        // Bound checking
        if (spawnChance <= 0 || spawnChance > 10)
            throw new RuntimeException("Tree spawn chance out of bounds: " + spawnChance + ". Must be between 0 (exclusive) and 10 (inclusive)");

        this.spawnChance = spawnChance;
        return this;
    }

    @Optional
    public @NotNull TreeBuilder addPlacementModifier(@NotNull PlacementModifier modifier) {
        Objects.requireNonNull(modifier, "Placement modifier must not be null");

        this.addedPlacementModifiers.add(modifier);
        return this;
    }

    @Optional
    public @NotNull TreeBuilder overrideDefaultPlacementModifiers() {
        this.overrideDefaultPlacementModifiers = true;
        return this;
    }

    @Optional
    public @NotNull TreeBuilder biomeSelector(@NotNull Predicate<BiomeSelectionContext> selector) {
        Objects.requireNonNull(selector, "Biome selector must not be null");

        this.biomeSelector = selector;
        return this;
    }

    /**
     * Builds the {@link ConfiguredTree}, which you can store and then register in your {@link ModInitializer}
     * with the {@link ConfiguredTree#register(Identifier)} method.
     */
    public @NotNull ConfiguredTree build() {
        // Verify all mandatory values
        verifyMandatory(trunkProvider, "trunkProvider");
        verifyMandatory(foliageProvider, "foliageProvider");
        verifyMandatory(trunkPlacer, "trunkPlacer");
        verifyMandatory(foliagePlacer, "foliagePlacer");

        // Create TreeFeatureConfig.Builder
        TreeFeatureConfig.Builder configBuilder = new TreeFeatureConfig.Builder
                (trunkProvider, trunkPlacer, foliageProvider, foliagePlacer, minimumSize);

        if (!decorators.isEmpty()) configBuilder.decorators(decorators);
        configBuilder.dirtProvider(dirtProvider);
        if (forceDirt) configBuilder.forceDirt();
        if (ignoreVines) configBuilder.ignoreVines();

        // Set up placement modifiers
        List<PlacementModifier> modifiers = Lists.newArrayList(
                RarityFilterPlacementModifier.of(spawnChance), // chance of the tree spawning
                SquarePlacementModifier.of(), // spread horizontally
                HeightmapPlacementModifier.of(Heightmap.Type.MOTION_BLOCKING) // in what heights can this tree spawn
        );

        if (overrideDefaultPlacementModifiers) modifiers.clear();

        modifiers.addAll(addedPlacementModifiers);

        // Create output
        ConfiguredFeature<?, ?> configured = Feature.TREE.configure(configBuilder.build());
        PlacedFeature placed = configured.withPlacement(modifiers);

        return new ConfiguredTree(configured, placed, biomeSelector);
    }

    /**
     * Builds your tree and immediately registers it.
     * <br><br>
     * <b>Do not use this in a field definition or a static initializer</b>, it will break!<br>
     * This is only useful if you build and register immediately in your {@link ModInitializer}'s code.
     *
     * @param id The tree's {@link Identifier}
     */
    public void buildAndRegister(@NotNull Identifier id) {
        build().register(id);
    }

    /**
     * Verifies a mandatory value or throws a {@link NullPointerException}
     *
     * @param mandatory The mandatory value
     * @param name The logged name of the value
     * @throws NullPointerException The exception thrown in case {@code mandatory} is null
     */
    private void verifyMandatory(@Nullable Object mandatory, @NotNull String name) throws NullPointerException {
        Objects.requireNonNull(mandatory, "Mandatory value " + name + " has not been set.");
    }

    /**
     * Indicates that the field is optional
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.METHOD})
    public @interface Optional {}

    /**
     * Indicates that the field is mandatory
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.METHOD})
    public @interface Mandatory {}

    /**
     * Indicates that this field will be <b>ignored</b> if default {@link PlacementModifier}s are disabled
     * with the use of {@link #overrideDefaultPlacementModifiers()}
     */
    public @interface EffectiveWithDefaultModifiersOnly {}
}
