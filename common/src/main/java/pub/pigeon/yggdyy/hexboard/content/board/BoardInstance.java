package pub.pigeon.yggdyy.hexboard.content.board;

import at.petrak.hexcasting.common.lib.HexSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation;

import java.util.ArrayList;
import java.util.List;

public class BoardInstance {
    public final Level level;
    public final List<Slot> slots = new ArrayList<>();
    public BoardBlockEntity mainEntity;
    public List<BoardBlockEntity> entities;
    public BoardInstance(@NotNull Level level, @NotNull BlockPos pos0, @NotNull BoardBlockEntity entity0) {
        this.level = level;
        new Builder(level, pos0).build(this);
    }
    @Nullable
    public static BoardInstance create(Level level, BlockPos pos) {
        if(level != null && level.getBlockEntity(pos) instanceof BoardBlockEntity entity) {
            return new BoardInstance(level, pos, entity);
        }
        return null;
    }
    public static class Slot {
        public BoardInstance board;
        public BoardBlockEntity entity;
        public int local;
        public Slot(BoardInstance board, BoardBlockEntity entity, int local) {
            this.board = board;
            this.entity = entity;
            this.local = local;
        }
        public ItemStack getStack() {
            return entity.typeblocks.get(local);
        }
        public void setStack(ItemStack stack) {
            entity.typeblocks.set(local, stack);
            entity.markDirty();
        }
    }
    protected static class Builder {
        protected Level level;
        protected BlockPos pos0;
        Vec3i f, y = new Vec3i(0, -1, 0), x;
        List<BoardBlockEntity> entities = new ArrayList<>();
        protected Builder(Level level, BlockPos pos0) {
            this.level = level;
            this.pos0 = pos0;
            f = level.getBlockState(pos0).getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal();
            x = f.cross(y);
            search(pos0);
        }
        private void search(BlockPos now) {
            if(level.getBlockEntity(now) instanceof BoardBlockEntity nowEntity && nowEntity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getNormal().equals(f) && !entities.contains(nowEntity)) {
                entities.add(nowEntity);
                List<BlockPos> nexts = List.of(now.offset(x), now.offset(x.multiply(-1)), now.offset(y), now.offset(y.multiply(-1)));
                for(BlockPos next : nexts) {
                    search(next);
                }
            }
        }
        public void build(BoardInstance board) {
            board.slots.clear();
            entities.sort((e1, e2) -> {
                BlockPos p1 = e1.getBlockPos(), p2 = e2.getBlockPos();
                int x1 = p1.getX(), y1 = p1.getY(), z1 = p1.getZ(), x2 = p2.getX(), y2 = p2.getY(), z2 = p2.getZ();
                int yC = Integer.compare(y2, y1);
                if(yC != 0) return yC;
                int xx1 = x.getX() * x1 + x.getZ() * z1, xx2 = x.getX() * x2 + x.getZ() * z2;
                return Integer.compare(xx1, xx2);
            });
            int pre = 0;
            for(int i = 0; i <= entities.size(); ++i) {
                if(i == entities.size() || entities.get(pre).getBlockPos().getY() != entities.get(i).getBlockPos().getY()) {
                    for(int j = 0; j < 4; ++j) {
                        for (int k = pre; k < i; ++k) {
                            for(int l = 0; l < 4; ++l) {
                                board.slots.add(new Slot(board, entities.get(k), j * 4 + l));
                            }
                        }
                    }
                    pre = i;
                }
            }
            board.mainEntity = entities.get(0);
            board.entities = this.entities;
        }
    }
    //server only
    public void applyOperation(Operation operation, Player operator) {
        if(operation.operate(this, operator, true) == Operation.OperateResult.SUCCESSFUL) {
            operation.operate(this, operator, false);
            operator.level().playSound(null, BlockPos.containing(operator.getEyePosition().add(operator.getLookAngle())), HexSounds.ADD_TO_PATTERN, SoundSource.BLOCKS, 1F, 1F);
        }
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof BoardInstance other) {
            if(this.entities.size() == other.entities.size()) {
                for(int i = 0; i < this.entities.size(); ++i) {
                    if(this.entities.get(i) != other.entities.get(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    public int findGlobalIdx(BoardBlockEntity e, int local) {
        for(int i = 0; i < slots.size(); ++i) {
            if(slots.get(i).entity == e && slots.get(i).local == local) {
                return i;
            }
        }
        return -1;
    }
}
