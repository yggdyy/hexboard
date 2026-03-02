package pub.pigeon.yggdyy.hexboard.content.interaction.operation;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.ListIota;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;
import pub.pigeon.yggdyy.hexboard.content.interaction.resolve.BoardResolveException;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.TypeblockItem;

import java.util.ArrayList;
import java.util.List;

public class ResolveOperation extends Operation{
    public static final Type<ResolveOperation> TYPE = new Type<>(
            RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("l").forGetter(o -> o.left),
                Codec.INT.fieldOf("r").forGetter(o -> o.right),
                Codec.BOOL.fieldOf("lit").forGetter(o -> o.literal)
            ).apply(instance, ResolveOperation::new)),
            HexBoard.modLoc("resolve")
    );
    public int left, right;
    boolean literal;
    public ResolveOperation(int left, int right, boolean literal) {
        this.left = Math.min(left, right);
        this.right = Math.max(left, right);
        this.literal = literal;
    }
    @Override
    public @NotNull OperateResult operate(BoardInstance board, Player player, boolean simulate) {
        if(0 <= left && right < board.slots.size()) {
            if(board.mainEntity.getLevel() == null || board.mainEntity.getLevel().isClientSide) {
                return OperateResult.SUCCESSFUL;
            }
            try {
                List<Iota> resolved = new ArrayList<>();
                ServerLevel level = (ServerLevel) board.mainEntity.getLevel();
                for(int i = left; i <= right; ++i) {
                    if(!board.slots.get(i).getStack().isEmpty() && board.slots.get(i).getStack().getItem() instanceof TypeblockItem item) {
                        if(literal) item.literalResolve(level, board, resolved, board.slots.get(i).getStack(), i);
                        else item.resolve(level, board, resolved, board.slots.get(i).getStack(), i);
                    }
                }
                ListIota res = new ListIota(resolved);
                List<ItemStack> hands = List.of(player.getMainHandItem(), player.getOffhandItem());
                for(ItemStack stack : hands) {
                    if(!stack.isEmpty() && stack.getItem() instanceof IotaHolderItem item) {
                        if(item.canWrite(stack, res)) {
                            if(!simulate) item.writeDatum(stack, res);
                            return OperateResult.SUCCESSFUL;
                        }
                    }
                }
                throw new BoardResolveException(Component.translatable("resolve.hexboard.exp_no_holder"));
            } catch (Exception exception) {
                if(exception instanceof BoardResolveException exp) {
                    player.sendSystemMessage(exp.msg);
                } else {
                    player.sendSystemMessage(Component.translatable("resolve.hexboard.exp_unknown", exception.getLocalizedMessage()));
                }
                return OperateResult.FAILED;
            }
        }
        return OperateResult.FAILED;
    }
    @Override
    public @NotNull OperateResult deOperate(BoardInstance board, Player player, boolean simulate) {
        return OperateResult.FAILED;
    }
    @Override
    public boolean shouldRecord() {
        return false;
    }
    @Override
    public Type<? extends Operation> getType() {
        return TYPE;
    }
}
