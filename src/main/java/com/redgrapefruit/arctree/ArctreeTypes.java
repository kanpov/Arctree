package com.redgrapefruit.arctree;

import com.mojang.serialization.Codec;
import com.redgrapefruit.arctree.mixin.BlockStateProviderTypeInvoker;
import com.redgrapefruit.arctree.mixin.FoliagePlacerTypeInvoker;
import com.redgrapefruit.arctree.mixin.TreeDecoratorTypeInvoker;
import com.redgrapefruit.arctree.mixin.TrunkPlacerTypeInvoker;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.floatprovider.FloatProviderType;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.IntProviderType;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.heightprovider.HeightProviderType;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.BlockStateProviderType;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import org.jetbrains.annotations.NotNull;

/**
 * The class for registering locked registry wrappers (Type's) for trees and other worldgen.
 * <br><br>
 * {@code IntProvider}s, {@code FloatProvider}s and {@code HeightProvider}s have a bit inconvenient registering methods,
 * so this class unifies them to use {@code Identifier}s.
 */
public final class ArctreeTypes {
    /**
     * Creates a {@code TrunkPlacerType} and registers it.
     *
     * @param id Registry {@code Identifier}
     * @param codec The {@code Codec} defined in the {@code TrunkPlacer}
     * @param <T> The {@code} TrunkPlacer
     * @return The created {@code TrunkPlacerType} for further use.
     */
    @NotNull
    public static <T extends TrunkPlacer> TrunkPlacerType<T> trunkPlacerType(@NotNull Identifier id, @NotNull Codec<T> codec) {
        return TrunkPlacerTypeInvoker.invokeRegister(id.toString(), codec);
    }

    /**
     * Creates a {@code FoliagePlacerType} and registers it.
     *
     * @param id Registry {@code Identifier}
     * @param codec The {@code Codec} defined in the {@code FoliagePlacer}
     * @param <T> The {@code FoliagePlacer}
     * @return The created {@code FoliagePlacerType} for further use.
     */
    @NotNull
    public static <T extends FoliagePlacer> FoliagePlacerType<T> foliagePlacerType(@NotNull Identifier id, @NotNull Codec<T> codec) {
        return FoliagePlacerTypeInvoker.invokeRegister(id.toString(), codec);
    }

    /**
     * Creates a {@code TreeDecoratorType} and registers it.
     *
     * @param id Registry {@code Identifier}
     * @param codec The {@code Codec} defined in the {@code TreeDecorator}
     * @param <T> The {@code TreeDecorator}
     * @return The created {@code TreeDecoratorType} for further use.
     */
    @NotNull
    public static <T extends TreeDecorator> TreeDecoratorType<T> treeDecoratorType(@NotNull Identifier id, @NotNull Codec<T> codec) {
        return TreeDecoratorTypeInvoker.invokeRegister(id.toString(), codec);
    }

    /**
     * Creates a {@code BlockStateProviderType} and registers it.
     *
     * @param id Registry {@code Identifier}
     * @param codec The {@code Codec} defined in the {@code BlockStateProvider}
     * @param <T> The {@code BlockStateProvider}
     * @return The created {@code BlockStateProviderType} for further use.
     */
    @NotNull
    public static <T extends BlockStateProvider> BlockStateProviderType<T> blockStateProviderType(@NotNull Identifier id, @NotNull Codec<T> codec) {
        return BlockStateProviderTypeInvoker.invokeRegister(id.toString(), codec);
    }

    /**
     * Wraps {@link IntProviderType#register} with an {@code Identifier}
     */
    @NotNull
    public static <T extends IntProvider> IntProviderType<T> intProviderType(@NotNull Identifier id, @NotNull Codec<T> codec) {
        return IntProviderType.register(id.toString(), codec);
    }

    /**
     * Wraps {@link FloatProviderType#register} with an {@code Identifier}
     */
    @NotNull
    public static <T extends FloatProvider> FloatProviderType<T> floatProviderType(@NotNull Identifier id, @NotNull Codec<T> codec) {
        return FloatProviderType.register(id.toString(), codec);
    }

    /**
     * Wraps {@link HeightProviderType#register} with an {@code Identifier}
     */
    @NotNull
    public static <T extends HeightProvider> HeightProviderType<T> heightProviderType(@NotNull Identifier id, @NotNull Codec<T> codec) {
        return HeightProviderType.register(id.toString(), codec);
    }
}
