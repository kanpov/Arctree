package com.redgrapefruit.arctree.kotlin

import com.google.common.collect.ImmutableList
import net.minecraft.block.Blocks
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.feature.size.FeatureSize
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.treedecorator.TreeDecorator
import net.minecraft.world.gen.trunk.TrunkPlacer

/**
 * The DSL scope for configuring your tree.
 */
class TreeScope internal constructor() {
    /**
     * A [BlockStateProvider] for the trunk of the tree
     */
    @Mandatory
    var trunkProvider: BlockStateProvider? = null

    /**
     * A [BlockStateProvider] for the foliage of the tree
     */
    @Mandatory
    var foliageProvider: BlockStateProvider? = null

    /**
     * A [BlockStateProvider] for the sapling that the tree can be planted with
     */
    @Mandatory
    var saplingProvider: BlockStateProvider? = null

    /**
     * The [TrunkPlacer] for this tree.
     */
    @Mandatory
    var trunkPlacer: TrunkPlacer? = null

    /**
     * The [FoliagePlacer] for this tree.
     */
    @Mandatory
    var foliagePlacer: FoliagePlacer? = null

    /**
     * The minimum [FeatureSize] for the tree.
     *
     * The optional value is recommended for use in most cases.
     */
    @Optional
    var minimumSize: FeatureSize = TwoLayersFeatureSize(1, 0, 1)

    /**
     * An [ImmutableList] of all [TreeDecorator]s for use in this tree.
     *
     * Empty by default.
     */
    @Optional
    var decorators: ImmutableList<TreeDecorator> = ImmutableList.of()

    /**
     * The [BlockStateProvider] for the dirt block generated underneath the tree.
     *
     * Minecraft's standard dirt block by default.
     */
    @Optional
    var dirtProvider: BlockStateProvider = SimpleBlockStateProvider(Blocks.DIRT.defaultState)

    /**
     * Lets the worldgen ignore vines in the way.
     *
     * `false` by default.
     */
    @Optional
    var ignoreVines: Boolean = false

    /**
     * Lets the worldgen force dirt to be generated underneath the tree.
     *
     * `false` by default.
     */
    @Optional
    var forceDirt: Boolean = false

    /**
     * The chance of the tree spawning in world.
     *
     * 3 or 33% by default.
     */
    @Optional
    var spawnChance: Int = 3

    internal fun build(): ConfiguredFeature<*, *> {
        // Check all mandatory fields
        trunkProvider!!; foliageProvider!!; saplingProvider!!; trunkPlacer!!; foliagePlacer!!

        // Create TreeFeatureConfig.Builder
        val builder = TreeFeatureConfig.Builder(
            trunkProvider, trunkPlacer, foliageProvider, saplingProvider, foliagePlacer, minimumSize)

        if (!decorators.isEmpty()) builder.decorators(decorators)
        builder.dirtProvider(dirtProvider)
        if (forceDirt) builder.forceDirt()
        if (ignoreVines) builder.ignoreVines()

        // Create the ConfiguredFeature and return it
        return Feature.TREE
            .configure(builder.build())
            .spreadHorizontally()
            .applyChance(spawnChance)
    }

    /**
     * Marks a parameter in the DSL as optional and provided with a default value
     */
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
    annotation class Optional

    /**
     * Marks a parameter in the DSL as mandatory and not having a default value
     */
    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
    annotation class Mandatory
}
