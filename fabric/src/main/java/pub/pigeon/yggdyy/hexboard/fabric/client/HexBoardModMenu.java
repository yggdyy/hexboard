package pub.pigeon.yggdyy.hexboard.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;

public class HexBoardModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(HexBoardConfig.Data.class, parent).get();
    }
}
