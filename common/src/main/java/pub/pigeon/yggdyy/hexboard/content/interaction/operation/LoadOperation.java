package pub.pigeon.yggdyy.hexboard.content.interaction.operation;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;
import pub.pigeon.yggdyy.hexboard.content.interaction.load.BoardLoadException;
import pub.pigeon.yggdyy.hexboard.content.interaction.load.Loaders;

import java.util.ArrayList;
import java.util.List;

public class LoadOperation extends Operation{
    public static final Type<LoadOperation> TYPE = new Type<>(
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("start").forGetter(o -> o.startIdx)
            ).apply(instance, LoadOperation::new)),
            HexBoard.modLoc("load")
    );
    public int startIdx = -1;
    public LoadOperation(int start) {
        startIdx = start;
    }
    @Override
    public @NotNull OperateResult operate(BoardInstance board, Player player, boolean simulate) {
        if(startIdx < 0 || startIdx >= board.slots.size()) {
            return OperateResult.FAILED;
        }
        List<ItemStack> froms = List.of(player.getMainHandItem(), player.getOffhandItem());
        CompoundTag rootTag = null;
        for(ItemStack from : froms) {
            if(!from.isEmpty() && from.getItem() instanceof IotaHolderItem item) {
                rootTag = item.readIotaTag(from);
                if(rootTag != null) {
                    break;
                }
            }
        }
        if(rootTag == null) {
            return OperateResult.FAILED;
        }
        if(board.mainEntity.getLevel() == null || board.mainEntity.getLevel().isClientSide) {
            return OperateResult.SUCCESSFUL;
        }
        try {
            ServerLevel level = (ServerLevel) board.mainEntity.getLevel();
            Iota root = IotaType.deserialize(rootTag, level);
            Iterable<Iota> toLoad = (root instanceof ListIota listIota)? listIota.getList() : List.of(root);
            List<ItemStack> toAdd = new ArrayList<>();
            /*for(Iota iota : toLoad) {
                boolean flag = false;
                for(Loaders.ILoader loader : Loaders.LOADERS) {
                    if(loader.canApply(iota)) {
                        loader.apply(toAdd, iota);
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    throw new BadIotaException(iota);
                }
            }*/
            Loaders.load(toLoad, toAdd);
            AddOperation addOperation = new AddOperation(toAdd, startIdx);
            if(simulate) {
                return addOperation.operate(board, player, simulate);
            } else {
                board.applyOperation(addOperation, player);
                return OperateResult.SUCCESSFUL;
            }
        } catch (Exception e) {
            if(e instanceof BoardLoadException ble) {
                player.sendSystemMessage(ble.msg);
            } else {
                player.sendSystemMessage(Component.translatable("load.hexboard.exp_unknown", e.getLocalizedMessage()));
            }
            return OperateResult.FAILED;
        }
    }
    @Override
    public @NotNull OperateResult deOperate(BoardInstance board, Player player, boolean simulate) {
        return OperateResult.FAILED;
    }
    @Override
    public Type<? extends Operation> getType() {
        return TYPE;
    }
    @Override
    public boolean shouldRecord() {
        return false;
    }
}
