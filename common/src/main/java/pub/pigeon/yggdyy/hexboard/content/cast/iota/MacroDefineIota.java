package pub.pigeon.yggdyy.hexboard.content.cast.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.utils.HexUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.content.ModIotaTypes;

public class MacroDefineIota extends Iota {
    public MacroDefineIota(@NotNull HexPattern pattern) {
        super(ModIotaTypes.MACRO_DEFINE, pattern);
    }
    protected MacroDefineIota(@NotNull IotaType<?> type, @NotNull Object payload) {
        super(type, payload);
    }
    public HexPattern getPattern() {
        return (HexPattern) payload;
    }
    @Override
    public boolean isTruthy() {
        return true;
    }
    @Override
    protected boolean toleratesOther(Iota that) {
        return that instanceof MacroDefineIota m && getPattern().getAngles().equals(m.getPattern().getAngles());
    }
    @Override
    public @NotNull Tag serialize() {
        return getPattern().serializeToNBT();
    }
    public static MacroDefineIota deserialize(Tag tag) throws IllegalArgumentException {
        var patTag = HexUtils.downcast(tag, CompoundTag.TYPE);
        HexPattern pat = HexPattern.fromNBT(patTag);
        return new MacroDefineIota(pat);
    }
}
