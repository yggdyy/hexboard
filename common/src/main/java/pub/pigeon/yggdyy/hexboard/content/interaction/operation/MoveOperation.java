package pub.pigeon.yggdyy.hexboard.content.interaction.operation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;

import java.util.ArrayList;
import java.util.List;

public class MoveOperation extends Operation{
    public static final Type<MoveOperation> TYPE = new Type<>(
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.INT.fieldOf("l").forGetter(o -> o.left),
                    Codec.INT.fieldOf("r").forGetter(o -> o.right),
                    Codec.INT.fieldOf("t").forGetter(o -> o.target)
            ).apply(instance, MoveOperation::new)),
            HexBoard.modLoc("move")
    );
    public int left, right, target;
    public MoveOperation(int l, int r, int t) {
        this.left = Math.min(l, r);
        this.right = Math.max(l, r);
        this.target = t;
    }
    @Override
    public @NotNull OperateResult operate(BoardInstance board, Player player, boolean simulate) {
        return op(board, simulate, left, right, target);
    }
    @Override
    public @NotNull OperateResult deOperate(BoardInstance board, Player player, boolean simulate) {
        return op(board, simulate, target, target + right - left, left);
    }
    public @NotNull OperateResult op(BoardInstance board, boolean simulate, int l, int r, int t) {
        int size = board.slots.size();
        if(l >= 0 && r < size && t >= 0 && t + r - l < size) {
            boolean flag = true;
            for(int i = t; i <= t + r - l; ++i) {
                if(l <= i && i <= r) {
                    continue;
                }
                if(!board.slots.get(i).getStack().isEmpty()) {
                    flag = false;
                    break;
                }
            }
            if(flag) {
                if(!simulate) {
                    List<ItemStack> toMove = new ArrayList<>();
                    for(int i = l; i <= r; ++i) {
                        toMove.add(board.slots.get(i).getStack().copy());
                        if(!board.slots.get(i).getStack().isEmpty()) {
                            board.slots.get(i).setStack(ItemStack.EMPTY);
                        }
                    }
                    for(int i = t; i <= t + r - l; ++i) {
                        board.slots.get(i).setStack(toMove.get(i - t));
                    }
                }
                return OperateResult.SUCCESSFUL;
            }
        }
        return OperateResult.FAILED;
    }
    @Override
    public Type<? extends Operation> getType() {
        return TYPE;
    }
}
