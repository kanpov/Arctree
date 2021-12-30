package com.redgrapefruit.arctree.kotlin

import com.redgrapefruit.arctree.ConfiguredTree
import com.redgrapefruit.arctree.TreeBuilder
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.world.gen.decorator.PlacementModifier
import net.minecraft.world.gen.feature.size.FeatureSize
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize
import net.minecraft.world.gen.foliage.FoliagePlacer
import net.minecraft.world.gen.stateprovider.BlockStateProvider
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.treedecorator.TreeDecorator
import net.minecraft.world.gen.trunk.TrunkPlacer
import java.util.function.Predicate

// Tree DSL

@DslMarker
annotation class TreeDSL

@TreeDSL
inline fun tree(action: TreeScope.() -> Unit): ConfiguredTree {
    val scope = TreeScope()
    scope.action()
    return scope.makeBuilder().build()
}

class TreeScope @PublishedApi internal constructor() {

    private var trunkPlacer: TrunkPlacer? = null
    private var trunkProvider: BlockStateProvider? = null
    private var foliagePlacer: FoliagePlacer? = null
    private var foliageProvider: BlockStateProvider? = null
    private var minimumSize: FeatureSize = TwoLayersFeatureSize(1, 0, 1)
    private var decorators: MutableList<TreeDecorator> = mutableListOf()
    private var dirtProvider: BlockStateProvider = SimpleBlockStateProvider.of(Blocks.DIRT.defaultState)
    private var ignoreVines: Boolean = false
    private var forceDirt: Boolean = false
    private var placementModifiers: MutableList<PlacementModifier> = mutableListOf()
    private var selector: Predicate<BiomeSelectionContext> = BiomeSelectors.foundInOverworld()
    private var overrideDefaultModifiers: Boolean = false
    private var spawnChance: Int = 3

    fun trunkPlacer(trunkPlacer: TrunkPlacer) {
        this.trunkPlacer = trunkPlacer
    }

    fun trunkBlock(provider: BlockStateProvider) {
        this.trunkProvider = provider
    }

    fun trunkBlock(block: Block) {
        this.trunkProvider = SimpleBlockStateProvider.of(block.defaultState)
    }

    fun trunkBlock(state: BlockState) {
        this.trunkProvider = SimpleBlockStateProvider.of(state)
    }

    fun foliagePlacer(foliagePlacer: FoliagePlacer) {
        this.foliagePlacer = foliagePlacer
    }

    fun foliageBlock(provider: BlockStateProvider) {
        this.foliageProvider = provider
    }

    fun foliageBlock(block: Block) {
        this.foliageProvider = SimpleBlockStateProvider.of(block.defaultState)
    }

    fun foliageBlock(state: BlockState) {
        this.foliageProvider = SimpleBlockStateProvider.of(state)
    }

    fun minimumSize(featureSize: FeatureSize) {
        this.minimumSize = featureSize
    }

    fun dirtBlock(provider: BlockStateProvider) {
        this.dirtProvider = provider
    }

    fun dirtBlock(block: Block) {
        this.dirtProvider = SimpleBlockStateProvider.of(block.defaultState)
    }

    fun dirtBlock(state: BlockState) {
        this.dirtProvider = SimpleBlockStateProvider.of(state)
    }

    fun minimumSize(limit: Int, lowerSize: Int, upperSize: Int) {
        this.minimumSize = TwoLayersFeatureSize(limit, lowerSize, upperSize)
    }

    fun ignoreVines() {
        this.ignoreVines = true
    }

    fun forceDirt() {
        this.forceDirt = true
    }

    fun decorator(decorator: TreeDecorator) {
        this.decorators.add(decorator)
    }

    fun decorators(list: Collection<TreeDecorator>) {
        this.decorators.addAll(list)
    }

    fun decorators(action: MutableList<TreeDecorator>.() -> Unit) {
        this.decorators.addAll(buildList(action))
    }

    fun modifier(modifier: PlacementModifier) {
        placementModifiers += modifier
    }

    fun modifiers(list: Collection<PlacementModifier>) {
        this.placementModifiers.addAll(list)
    }

    fun modifiers(action: MutableList<PlacementModifier>.() -> Unit) {
        this.placementModifiers.addAll(buildList(action))
    }

    fun selector(selector: Predicate<BiomeSelectionContext>) {
        this.selector = selector
    }

    fun overrideDefaultModifiers() {
        overrideDefaultModifiers = true
    }

    @TreeBuilder.EffectiveWithDefaultModifiersOnly
    fun spawnChance(chance: Int) {
        spawnChance = chance
    }

    @PublishedApi
    internal fun makeBuilder(): TreeBuilder {
        val builder = TreeBuilder.create()

        builder.trunkProvider(trunkProvider!!)
        builder.foliageProvider(foliageProvider!!)
        builder.trunkPlacer(trunkPlacer!!)
        builder.foliagePlacer(foliagePlacer!!)
        builder.minimumSize(minimumSize)
        decorators.forEach(builder::addDecorator)
        builder.dirtProvider(dirtProvider)
        if (ignoreVines) builder.ignoreVines()
        if (forceDirt) builder.forceDirt()
        builder.spawnChance(spawnChance)
        placementModifiers.forEach(builder::addPlacementModifier)
        if (overrideDefaultModifiers) builder.overrideDefaultPlacementModifiers()
        builder.biomeSelector(selector)

        return builder
    }
}
