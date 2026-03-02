package pub.pigeon.yggdyy.hexboard.content.interaction.resolve;

import net.minecraft.network.chat.Component;

public class TooManyIotaException extends BoardResolveException{
    public TooManyIotaException(int index) {
        super(Component.translatable("resolve.hexboard.exp_too_many_iota", index));
    }
}
