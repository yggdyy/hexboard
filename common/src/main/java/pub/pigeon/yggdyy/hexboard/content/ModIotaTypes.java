package pub.pigeon.yggdyy.hexboard.content;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.utils.HexUtils;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.cast.iota.MacroCallIota;
import pub.pigeon.yggdyy.hexboard.content.cast.iota.MacroDefineIota;

public class ModIotaTypes {
    public static final IotaType<MacroDefineIota> MACRO_DEFINE = new IotaType<MacroDefineIota>() {
        @Override
        public MacroDefineIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            return MacroDefineIota.deserialize(tag);
        }
        @Override
        public Component display(Tag tag) {
            return PatternIota.display(HexPattern.fromNBT(HexUtils.downcast(tag, CompoundTag.TYPE)));
        }
        @Override
        public int color() {
            return 0x0000FF;
        }
    };
    public static final IotaType<MacroCallIota> MACRO_CALL = new IotaType<MacroCallIota>() {
        @Override
        public MacroCallIota deserialize(Tag tag, ServerLevel world) throws IllegalArgumentException {
            return MacroCallIota.deserialize(tag);
        }
        @Override
        public Component display(Tag tag) {
            return PatternIota.display(HexPattern.fromNBT(HexUtils.downcast(tag, CompoundTag.TYPE)));
        }
        @Override
        public int color() {
            return 0xFF0000;
        }
    };
    private static void register(ResourceLocation id, IotaType<?> type) {
        Registry.register(HexIotaTypes.REGISTRY, id, type);
    }
    public static void init() {
        register(HexBoard.modLoc("macro_define"), MACRO_DEFINE);
        register(HexBoard.modLoc("macro_call"), MACRO_CALL);
    }
}
