package pub.pigeon.yggdyy.hexboard.content.interaction.resolve;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.server.level.ServerLevel;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.TypeblockItem;

import java.util.List;

public class ResolveHandler {
    public static void resolve(BoardInstance board, ServerLevel level, int left, int right, List<Iota> resolved, boolean literal) throws Exception {
        for(int i = left; i <= right; ++i) {
            if(!board.slots.get(i).getStack().isEmpty() && board.slots.get(i).getStack().getItem() instanceof TypeblockItem item) {
                if(literal) item.literalResolve(level, board, resolved, board.slots.get(i).getStack(), i);
                else item.resolve(level, board, resolved, board.slots.get(i).getStack(), i);
            }
        }
    }
}
