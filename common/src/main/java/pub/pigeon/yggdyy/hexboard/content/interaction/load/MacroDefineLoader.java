package pub.pigeon.yggdyy.hexboard.content.interaction.load;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.world.item.ItemStack;
import pub.pigeon.yggdyy.hexboard.content.ModIotaTypes;
import pub.pigeon.yggdyy.hexboard.content.ModItems;

import java.util.List;

public class MacroDefineLoader implements Loaders.ILoader{
    @Override
    public boolean canApply(Iota iota) {
        return iota.getType().equals(ModIotaTypes.MACRO_DEFINE);
    }
    @Override
    public void apply(List<ItemStack> toAdd, Iota iota) {
        ItemStack stack = new ItemStack(ModItems.LAPIS_TYPEBLOCK.get());
        ModItems.LAPIS_TYPEBLOCK.get().writeDatum(stack, iota);
        toAdd.add(stack);
    }
}
