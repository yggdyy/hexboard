package pub.pigeon.yggdyy.hexboard;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.pigeon.yggdyy.hexboard.content.*;
import pub.pigeon.yggdyy.hexboard.content.interaction.load.Loaders;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operations;

public final class HexBoard {
    public static final String MOD_ID = "hexboard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static void init() {
        AutoConfig.register(HexBoardConfig.Data.class, Toml4jConfigSerializer::new);
        AutoConfig.getConfigHolder(HexBoardConfig.Data.class).registerSaveListener((manager, data) -> {
            HexBoardConfig.config = manager.get();
            return InteractionResult.SUCCESS;
        });
        HexBoardConfig.config = AutoConfig.getConfigHolder(HexBoardConfig.Data.class).get();
        ModBlocks.register();
        ModItems.register();
        ModC2SHandlers.init();
        Operations.init();
        Loaders.init();
    }
    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
