package pub.pigeon.yggdyy.hexboard.content.interaction.load;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.world.item.ItemStack;
import pub.pigeon.yggdyy.hexboard.content.ModItems;

import java.util.List;

public class PatternLoader implements Loaders.ILoader{
    @Override
    public boolean canApply(Iota iota) {
        return iota.getType().equals(HexIotaTypes.PATTERN);
    }
    @Override
    public void apply(List<ItemStack> toAdd, Iota iota) {
        assert iota instanceof PatternIota;
        PatternIota patIota = (PatternIota) iota;
        ItemStack s = new ItemStack(ModItems.QUARTZ_TYPEBLOCK.get());
        ModItems.QUARTZ_TYPEBLOCK.get().writeDatum(s, patIota);
        toAdd.add(s);
    }
}
