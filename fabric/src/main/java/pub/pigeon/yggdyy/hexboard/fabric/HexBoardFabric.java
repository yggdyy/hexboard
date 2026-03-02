package pub.pigeon.yggdyy.hexboard.fabric;

import pub.pigeon.yggdyy.hexboard.HexBoard;
import net.fabricmc.api.ModInitializer;

public final class HexBoardFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HexBoard.init();
    }
}
