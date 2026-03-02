package pub.pigeon.yggdyy.hexboard.content.board;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.content.ModBlocks;
import pub.pigeon.yggdyy.hexboard.util.ItemStackUtil;

public class BoardBlockEntity extends BlockEntity {
    public final NonNullList<ItemStack> typeblocks = NonNullList.withSize(16, ItemStack.EMPTY);
    public BoardBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.BOARD_ENTITY.get(), blockPos, blockState);
    }
    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ItemStackUtil.saveAllItems(compoundTag, typeblocks, true);
    }
    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        ContainerHelper.loadAllItems(compoundTag, typeblocks);
    }
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
    public void markDirty() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }
}
