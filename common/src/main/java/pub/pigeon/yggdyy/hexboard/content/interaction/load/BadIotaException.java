package pub.pigeon.yggdyy.hexboard.content.interaction.load;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.network.chat.Component;

public class BadIotaException extends BoardLoadException{
    public final Iota iota;
    public BadIotaException(Iota iota) {
        super(Component.translatable("load.hexboard.exp_bad_iota", iota.display()));
        this.iota = iota;
    }
}
