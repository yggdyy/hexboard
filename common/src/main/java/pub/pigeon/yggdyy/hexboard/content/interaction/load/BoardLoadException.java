package pub.pigeon.yggdyy.hexboard.content.interaction.load;

import net.minecraft.network.chat.Component;

public class BoardLoadException extends Exception{
    public final Component msg;
    public BoardLoadException(Component msg) {
        this.msg = msg;
    }
}
