package pub.pigeon.yggdyy.hexboard.content.typeblock.items;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.ModIotaTypes;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;
import pub.pigeon.yggdyy.hexboard.content.cast.iota.MacroCallIota;
import pub.pigeon.yggdyy.hexboard.content.cast.iota.MacroDefineIota;

import java.util.List;

public class LapisTypeblockItem extends TypeblockItem implements IotaHolderItem {
    public static final ResourceLocation ID = HexBoard.modLoc("lapis_typeblock");
    private static final String IOTA_TAG = "iota";
    public LapisTypeblockItem(Properties properties) {
        super(properties);
    }
    @Override
    public ResourceLocation getRendererID() {
        return ID;
    }
    @Override
    public void literalResolve(ServerLevel level, BoardInstance board, List<Iota> resolved, ItemStack stack, int idx) throws Exception {
        Iota iota = readIota(stack, level);
        if(iota != null) {
            resolved.add(iota);
        }
    }
    @Override
    public @Nullable CompoundTag readIotaTag(ItemStack stack) {
        return NBTHelper.getCompound(stack, IOTA_TAG);
    }
    @Override
    public boolean writeable(ItemStack stack) {
        return true;
    }
    @Override
    public boolean canWrite(ItemStack stack, @Nullable Iota iota) {
        return iota == null || iota.getType().equals(HexIotaTypes.PATTERN) || iota.getType().equals(ModIotaTypes.MACRO_DEFINE) || iota.getType().equals(ModIotaTypes.MACRO_CALL);
    }
    @Override
    public void writeDatum(ItemStack stack, @Nullable Iota iota) {
        if(iota == null) {
            stack.removeTagKey(IOTA_TAG);
        } else {
            MacroDefineIota dIota = null;
            if(iota instanceof PatternIota pIota) {
                dIota = new MacroDefineIota(pIota.getPattern());
            } else if(iota instanceof MacroCallIota cIota) {
                dIota = new MacroDefineIota(cIota.getPattern());
            }
            NBTHelper.put(stack, IOTA_TAG, IotaType.serialize(dIota == null ? iota : dIota));
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag flag) {
        IotaHolderItem.appendHoverText(this, stack, tips, flag);
    }
}
