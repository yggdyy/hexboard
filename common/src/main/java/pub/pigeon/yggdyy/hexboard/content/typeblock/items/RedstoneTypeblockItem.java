package pub.pigeon.yggdyy.hexboard.content.typeblock.items;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
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
import pub.pigeon.yggdyy.hexboard.content.ModItems;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;
import pub.pigeon.yggdyy.hexboard.content.cast.iota.MacroCallIota;
import pub.pigeon.yggdyy.hexboard.content.cast.iota.MacroDefineIota;

import java.util.List;
import java.util.Objects;

public class RedstoneTypeblockItem extends TypeblockItem implements IotaHolderItem {
    public static final ResourceLocation ID = HexBoard.modLoc("redstone_typeblock");
    private static final String IOTA_TAG = "iota";
    public RedstoneTypeblockItem(Properties properties) {
        super(properties);
    }
    @Override
    public ResourceLocation getRendererID() {
        return ID;
    }
    @Override
    public void resolve(ServerLevel level, BoardInstance board, List<Iota> resolved, ItemStack stack, int idx) throws Exception {
        super.resolve(level, board, resolved, stack, idx);
        HexPattern reqPat = ((MacroCallIota) Objects.requireNonNull(ModItems.REDSTONE_TYPEBLOCK.get().readIota(stack, level))).getPattern();
        for(int i = 0; i < board.slots.size(); ++i) {
            ItemStack now = board.slots.get(i).getStack();
            if(!now.isEmpty() && now.getItem() instanceof LapisTypeblockItem lapis) {
                if(lapis.readIota(now, level) instanceof MacroDefineIota dIota && Objects.equals(dIota.getPattern(), reqPat)) {
                    for (int j = i + 1; j < board.slots.size(); ++j) {
                        ItemStack now1 = board.slots.get(j).getStack();
                        if(now1.isEmpty() || now1.getItem() instanceof LapisTypeblockItem) {
                            break;
                        }
                        if(now1.getItem() instanceof TypeblockItem typeblock) {
                            typeblock.resolve(level, board, resolved, now1, j);
                        }
                    }
                }
            }
        }
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
        return iota == null || iota.getType().equals(HexIotaTypes.PATTERN) || iota.getType().equals(ModIotaTypes.MACRO_CALL) || iota.getType().equals(ModIotaTypes.MACRO_DEFINE);
    }
    @Override
    public void writeDatum(ItemStack stack, @Nullable Iota iota) {
        if (iota == null) {
            stack.removeTagKey(IOTA_TAG);
        } else {
            MacroCallIota cIota = null;
            if (iota instanceof PatternIota pIota) {
                cIota = new MacroCallIota(pIota.getPattern());
            } else if(iota instanceof MacroDefineIota dIota) {
                cIota = new MacroCallIota(dIota.getPattern());
            }
            NBTHelper.put(stack, IOTA_TAG, IotaType.serialize(cIota == null ? iota : cIota));
        }
    }
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tips, TooltipFlag flag) {
        IotaHolderItem.appendHoverText(this, stack, tips, flag);
    }
}
