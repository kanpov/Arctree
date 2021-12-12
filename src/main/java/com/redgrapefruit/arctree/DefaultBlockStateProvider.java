package com.redgrapefruit.arctree;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.BlockStateProviderType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

public class DefaultBlockStateProvider extends BlockStateProvider {
    // What kind of unholy black magic is this sh*t?
    private static final Codec<DefaultBlockStateProvider> CODEC = BlockState.CODEC
            .fieldOf("state")
            .xmap(DefaultBlockStateProvider::new, (bsp) -> bsp.state)
            .codec();

    private static final BlockStateProviderType<DefaultBlockStateProvider> TYPE =
            ArctreeTypes.blockStateProviderType(new Identifier("arctree", "default_bsp"), CODEC);

    private final @NotNull BlockState state;

    public DefaultBlockStateProvider(@NotNull BlockState state) {
        Objects.requireNonNull(state, "BlockState can't be null");

        this.state = state;
    }

    @Override
    protected BlockStateProviderType<?> getType() {
        return TYPE;
    }

    @Override
    public BlockState getBlockState(Random random, BlockPos pos) {
        return state;
    }
}
