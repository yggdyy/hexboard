package pub.pigeon.yggdyy.hexboard.content.interaction.load;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.world.item.ItemStack;
import pub.pigeon.yggdyy.hexboard.content.ModItems;

import java.util.List;

public class OtherLoader implements Loaders.ILoader {
    @Override
    public boolean canApply(Iota iota) {
        return true;
    }
    @Override
    public void apply(List<ItemStack> toAdd, Iota iota) {
        ItemStack s = new ItemStack(ModItems.WOOL_TYPEBLOCK.get());
        ModItems.WOOL_TYPEBLOCK.get().writeDatum(s, iota);
        toAdd.add(s);
    }
}
