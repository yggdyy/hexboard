package pub.pigeon.yggdyy.hexboard.content.interaction.operation;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;

public abstract class Operation {
    @NotNull
    public abstract OperateResult operate(BoardInstance board, Player player, boolean simulate);
    @NotNull
    public abstract OperateResult deOperate(BoardInstance board, Player player, boolean simulate);
    public boolean shouldRecord() {
        return true;
    }
    public abstract Type<? extends Operation> getType();
    public enum OperateResult{
        SUCCESSFUL,
        FAILED
    }
    public static class Type<T extends Operation> {
        public final Codec<T> CODEC;
        public final ResourceLocation ID;
        public Type(Codec<T> codec, ResourceLocation id) {
            CODEC = codec;
            ID = id;
        }
    }
}
