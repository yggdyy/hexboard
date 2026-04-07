package pub.pigeon.yggdyy.hexboard.content.interaction.operation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;
import pub.pigeon.yggdyy.hexboard.util.ItemStackUtil;

public class ModifyOperation extends Operation{
    public static final Type<ModifyOperation> TYPE = new Type<>(
            RecordCodecBuilder.create(instance -> instance.group(
                    ItemStack.CODEC.fieldOf("from").forGetter(op -> op.from),
                    ItemStack.CODEC.fieldOf("to").forGetter(op -> op.to),
                    Codec.INT.fieldOf("target").forGetter(op -> op.target)
            ).apply(instance, ModifyOperation::new)),
            HexBoard.modLoc("modify")
    );
    public ItemStack from, to;
    public int target;
    public ModifyOperation(ItemStack from, ItemStack to, int target) {
        this.from = from;
        this.to = to;
        this.target = target;
    }
    @Override
    public @NotNull OperateResult operate(BoardInstance board, Player player, boolean simulate) {
        return op(board, player, simulate, from, to);
    }
    @Override
    public @NotNull OperateResult deOperate(BoardInstance board, Player player, boolean simulate) {
        return op(board, player, simulate, to, from);
    }
    private @NotNull OperateResult op(BoardInstance board, Player player, boolean simulate, ItemStack pFrom, ItemStack pTo) {
        if(target > -1 && target < board.slots.size() && !pTo.isEmpty() && ItemStackUtil.compareIgnoreNbt(pFrom, board.slots.get(target).getStack())) {
            if(!simulate) {
                board.slots.get(target).setStack(pTo.copy());
            }
            return OperateResult.SUCCESSFUL;
        }
        return OperateResult.FAILED;
    }
    @Override
    public Type<? extends Operation> getType() {
        return TYPE;
    }
}
