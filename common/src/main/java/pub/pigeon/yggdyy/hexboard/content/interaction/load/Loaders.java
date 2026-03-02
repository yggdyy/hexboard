package pub.pigeon.yggdyy.hexboard.content.interaction.load;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Loaders {
    public interface ILoader {
        boolean canApply(Iota iota);
        void apply(List<ItemStack> toAdd, Iota iota);
    }
    public static final List<ILoader> LOADERS = new ArrayList<>();
    public static void init() {
        LOADERS.add(new PatternLoader());
        LOADERS.add(new OtherLoader());
    }
}
