package pub.pigeon.yggdyy.hexboard.fabric;

import pub.pigeon.yggdyy.hexboard.HexBoard;
import net.fabricmc.api.ModInitializer;
import pub.pigeon.yggdyy.hexboard.content.ModIotaTypes;
import pub.pigeon.yggdyy.hexboard.content.ModPatterns;

public final class HexBoardFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        HexBoard.init();
        ModIotaTypes.init();
        ModPatterns.init();
    }
}
