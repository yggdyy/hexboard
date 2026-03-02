package pub.pigeon.yggdyy.hexboard.content;

import dev.architectury.networking.NetworkManager;
import net.minecraft.resources.ResourceLocation;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.ServerOperationHandler;

public class ModC2SHandlers {
    public static final ResourceLocation OPERATION = HexBoard.modLoc("operation");
    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, OPERATION, new ServerOperationHandler());
    }
}
