package pub.pigeon.yggdyy.hexboard.content.board;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoardBlock extends Block implements EntityBlock {
    public BoardBlock(Properties properties) {
        super(properties);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BoardBlockEntity(blockPos, blockState);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }
    @Override
    public @NotNull VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case DOWN, UP -> Shapes.block();
            case NORTH -> box(0, 0, 8, 16, 16, 16);
            case SOUTH -> box(0, 0, 0, 16, 16, 8);
            case WEST -> box(8, 0, 0, 16, 16, 16);
            case EAST -> box(0, 0, 0, 8, 16, 16);
        };
    }
}
