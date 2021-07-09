package com.redgrapefruit.arctree;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
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
import java.util.Arrays;
import java.util.Objects;

/**
 * An easy-to-use builder for tree {@code ConfiguredFeature}s.
 *
 * This is technically optional, but highly recommended to use.
 */
public final class ArctreeCreator {
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
     * The {@code BlockStateProvider} for the tree's sapling
     */
    @Mandatory
    @Nullable
    private BlockStateProvider saplingProvider;

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
     *
     * The optional value is recommended to use.
     */
    @Optional
    @NotNull
    private FeatureSize minimumSize = new TwoLayersFeatureSize(1, 0, 1);

    /**
     * An {@code ImmutableList} of all {@code TreeDecorator}s used in this tree.
     *
     * Empty by default.
     */
    @Optional
    @NotNull
    private ImmutableList<TreeDecorator> decorators = ImmutableList.of();

    /**
     * The {@code BlockStateProvider} for dirt generated underneath the tree.
     *
     * The standard Minecraft dirt block by default.
     */
    @Optional
    @NotNull
    private BlockStateProvider dirtProvider = new SimpleBlockStateProvider(Blocks.DIRT.getDefaultState());

    /**
     * Does this tree ignore vine generation.
     *
     * {@code false} by default.
     */
    @Optional
    private boolean ignoreVines = false;

    /**
     * Place dirt forcefully.
     *
     * {@code false} by default.
     */
    @Optional
    private boolean forceDirt = false;

    /**
     * The chance of spawning the tree from 0 to 10.
     *
     * 3 or 33% by default.
     */
    @Optional
    private int spawnChance = 3;

    /**
     * This class cannot be instantiated from elsewhere.
     *
     * Use {@link #create}
     */
    private ArctreeCreator() {}

    /**
     * Creates a new {@code ArctreeCreator}
     *
     * @return Created {@code ArctreeCreator}
     */
    @NotNull
    public static ArctreeCreator create() {
        return new ArctreeCreator();
    }

    /**
     * Creates an {@code ImmutableList} of {@code TreeDecorator}s in an easy way.
     *
     * @param decorators All {@code TreeDecorator}s for use with this tree
     * @return Built {@link ImmutableList}
     */
    @NotNull
    public static ImmutableList<TreeDecorator> decoratorList(@NotNull TreeDecorator... decorators) {
        return ImmutableList.copyOf(decorators);
    }

    /**
     * Builder for {@link #trunkPlacer}
     */
    @Mandatory
    @NotNull
    public ArctreeCreator trunkPlacer(@NotNull TrunkPlacer trunkPlacer) {
        this.trunkPlacer = trunkPlacer;
        return this;
    }

    /**
     * Builder for {@link #foliagePlacer}
     */
    @Mandatory
    @NotNull
    public ArctreeCreator foliagePlacer(@NotNull FoliagePlacer foliagePlacer) {
        this.foliagePlacer = foliagePlacer;
        return this;
    }

    /**
     * Builder for {@link #foliagePlacer}
     */
    @Mandatory
    @NotNull
    public ArctreeCreator trunkProvider(@NotNull BlockStateProvider trunkProvider) {
        this.trunkProvider = trunkProvider;
        return this;
    }

    /**
     * Builder for {@link #foliageProvider}
     */
    @Mandatory
    @NotNull
    public ArctreeCreator foliageProvider(@NotNull BlockStateProvider foliageProvider) {
        this.foliageProvider = foliageProvider;
        return this;
    }

    /**
     * Builder for {@link #saplingProvider}
     */
    @Mandatory
    @NotNull
    public ArctreeCreator saplingProvider(@NotNull BlockStateProvider saplingProvider) {
        this.saplingProvider = saplingProvider;
        return this;
    }

    /**
     * Builder for {@link #minimumSize}
     */
    @Optional
    @NotNull
    public ArctreeCreator minimumSize(@NotNull FeatureSize minimumSize) {
        this.minimumSize = minimumSize;
        return this;
    }

    /**
     * Builder for {@link #decorators}
     */
    @Optional
    @NotNull
    public ArctreeCreator decorators(@NotNull ImmutableList<TreeDecorator> decorators) {
        this.decorators = decorators;
        return this;
    }

    /**
     * Builder for {@link #dirtProvider}
     */
    @Optional
    @NotNull
    public ArctreeCreator dirtProvider(@NotNull BlockStateProvider dirtProvider) {
        this.dirtProvider = dirtProvider;
        return this;
    }

    /**
     * Builder for {@link #ignoreVines}
     */
    @Optional
    @NotNull
    public ArctreeCreator ignoreVines() {
        this.ignoreVines = true;
        return this;
    }

    /**
     * Builder for {@link #forceDirt}
     */
    @Optional
    @NotNull
    public ArctreeCreator forceDirt() {
        this.forceDirt = true;
        return this;
    }

    /**
     * Builder for {@link #spawnChance}
     */
    @Optional
    @NotNull
    public ArctreeCreator spawnChance(int spawnChance) {
        this.spawnChance = spawnChance;
        return this;
    }

    /**
     * Builds the actual {@link ConfiguredFeature}
     * @return Resulting {@link ConfiguredFeature}
     */
    @NotNull
    public ConfiguredFeature<?, ?> build() {
        // Verify all mandatory values
        verifyMandatory(trunkProvider, "trunkProvider");
        verifyMandatory(foliageProvider, "foliageProvider");
        verifyMandatory(saplingProvider, "saplingProvider");
        verifyMandatory(trunkPlacer, "trunkPlacer");
        verifyMandatory(foliagePlacer, "foliagePlacer");

        // Create TreeFeatureConfig.Builder
        TreeFeatureConfig.Builder configBuilder = new TreeFeatureConfig.Builder
                (trunkProvider, trunkPlacer, foliageProvider, saplingProvider, foliagePlacer, minimumSize);

        if (!decorators.isEmpty()) configBuilder.decorators(decorators);
        configBuilder.dirtProvider(dirtProvider);
        if (forceDirt) configBuilder.forceDirt();
        if (ignoreVines) configBuilder.ignoreVines();

        // Create the ConfiguredFeature and return it

       return Feature.TREE
                .configure(configBuilder.build())
                .spreadHorizontally()
                .applyChance(spawnChance);
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
}
