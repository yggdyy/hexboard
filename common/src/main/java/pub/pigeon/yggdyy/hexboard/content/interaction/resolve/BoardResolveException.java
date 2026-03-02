package pub.pigeon.yggdyy.hexboard.content.interaction.resolve;

import net.minecraft.network.chat.Component;

public class BoardResolveException extends Exception{
    public final Component msg;
    public BoardResolveException(Component msg) {
        this.msg = msg;
    }
}
