package pub.pigeon.yggdyy.hexboard.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import pub.pigeon.yggdyy.hexboard.HexBoardClient;

public class HexBoardModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return HexBoardClient::getConfigScreen;
    }
}
