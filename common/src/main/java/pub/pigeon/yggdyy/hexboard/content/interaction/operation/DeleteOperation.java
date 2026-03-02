package pub.pigeon.yggdyy.hexboard.content.interaction.operation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;

import java.util.List;

public class DeleteOperation extends AddOperation{
    public static final Type<DeleteOperation> TYPE = new Type<>(
            RecordCodecBuilder.create(instance -> instance.group(
                    ItemStack.CODEC.listOf().fieldOf("to_delete").forGetter(op -> op.toAdd),
                    Codec.INT.fieldOf("start_idx").forGetter(op -> op.startIdx)
            ).apply(instance, DeleteOperation::new)),
            HexBoard.modLoc("delete")
    );
    public DeleteOperation(List<ItemStack> toDelete, int startIdx) {
        super(toDelete, startIdx);
    }
    @Override
    public @NotNull OperateResult operate(BoardInstance board, Player player, boolean simulate) {
        return super.deOperate(board, player, simulate);
    }
    @Override
    public @NotNull OperateResult deOperate(BoardInstance board, Player player, boolean simulate) {
        return super.operate(board, player, simulate);
    }
    @Override
    public Type<? extends Operation> getType() {
        return TYPE;
    }
}
