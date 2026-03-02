package pub.pigeon.yggdyy.hexboard.content.typeblock.items;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.resolve.TooManyIotaException;

import java.util.List;

public abstract class TypeblockItem extends Item {
    public TypeblockItem(Properties properties) {
        super(properties);
    }
    public abstract ResourceLocation getRendererID();
    //server only
    public void resolve(ServerLevel level, BoardInstance board, List<Iota> resolved, ItemStack stack, int idx) throws Exception {
        if(resolved.size() >= HexBoardConfig.config.maxResolvedIota) {
            throw new TooManyIotaException(idx);
        }
    }
    //server only
    public void literalResolve(ServerLevel level, BoardInstance board, List<Iota> resolved, ItemStack stack, int idx) throws Exception{
        resolve(level, board, resolved, stack, idx);
    }
    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        if(useOnContext.getLevel().isClientSide) {
            return BoardClient.useWithTypeblockItem(useOnContext);
        }
        return InteractionResult.PASS;
    }
}
