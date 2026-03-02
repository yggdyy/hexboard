package pub.pigeon.yggdyy.hexboard.content.interaction.operation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;

import static pub.pigeon.yggdyy.hexboard.util.ItemStackUtil.*;

import java.util.List;

public class AddOperation extends Operation {
    public static final Type<AddOperation> TYPE = new Type<>(
            RecordCodecBuilder.create(instance -> instance.group(
                    ItemStack.CODEC.listOf().fieldOf("to_add").forGetter(op -> op.toAdd),
                    Codec.INT.fieldOf("start_idx").forGetter(op -> op.startIdx)
            ).apply(instance, AddOperation::new)),
            HexBoard.modLoc("add")
    );
    public List<ItemStack> toAdd;
    public int startIdx;
    public AddOperation(List<ItemStack> toAdd, int startIdx) {
        this.toAdd = toAdd;
        this.startIdx = startIdx;
    }
    @Override
    public @NotNull OperateResult operate(BoardInstance board, Player player, boolean simulate) {
        if(startIdx < 0 || board.slots.size() < startIdx + toAdd.size()) {
            return OperateResult.FAILED;
        }
        Inventory inventory = player.getInventory();
        List<ItemStack> stacks = simulate? copy(getAllItemStacks(inventory)) : getAllItemStacks(inventory);
        for(int i = startIdx; i < startIdx + toAdd.size(); ++i) {
            if(!board.slots.get(i).getStack().isEmpty())
                return OperateResult.FAILED;
        }
        for(ItemStack need : toAdd) {
            ItemStack _need = need.copy();
            if(consume(stacks, _need) > 0 && consumeIgnoreNbt(stacks, _need) > 0) return OperateResult.FAILED;
        }
        if(!simulate) {
            for (int i = startIdx; i < startIdx + toAdd.size(); ++i) {
                board.slots.get(i).setStack(toAdd.get(i - startIdx).copy());
            }
        }
        return OperateResult.SUCCESSFUL;
    }
    @Override
    public @NotNull OperateResult deOperate(BoardInstance board, Player player, boolean simulate) {
        if(startIdx < 0 || board.slots.size() < startIdx + toAdd.size()) {
            return OperateResult.FAILED;
        }
        if(!simulate) {
            for (int i = startIdx; i < startIdx + toAdd.size(); ++i) {
                if(!board.slots.get(i).getStack().isEmpty()) {
                    ItemEntity drop = new ItemEntity(player.level(), player.position().x, player.position().y, player.position().z, board.slots.get(i).getStack().copy());
                    player.level().addFreshEntity(drop);
                }
                board.slots.get(i).setStack(ItemStack.EMPTY);
            }
        }
        return OperateResult.SUCCESSFUL;
    }
    @Override
    public Type<? extends Operation> getType() {
        return TYPE;
    }
}
