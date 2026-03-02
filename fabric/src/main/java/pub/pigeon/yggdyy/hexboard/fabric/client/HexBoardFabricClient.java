package pub.pigeon.yggdyy.hexboard.fabric.client;

import at.petrak.hexcasting.fabric.event.MouseScrollCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.HexBoardClient;
import pub.pigeon.yggdyy.hexboard.content.ModModels;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.staff.StaffModes;

import java.util.HashMap;

public class HexBoardFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HexBoardClient.init();
        ModelLoadingPlugin.register(context -> {
            context.addModels(ModModels.IDS);
            context.modifyModelAfterBake().register((model, context1) -> {
                if(ModModels.CUSTOMS == null) {
                    ModModels.CUSTOMS = new HashMap<>();
                }
                if(context1.id().getNamespace().equals(HexBoard.MOD_ID)) {
                    ModModels.CUSTOMS.put(context1.id(), model);
                }
                return model;
            });
        });
        MouseScrollCallback.EVENT.register(StaffModes::handleScroll);
        WorldRenderEvents.BEFORE_DEBUG_RENDER.register(context -> BoardClient.renderWorldlyInfo(context.matrixStack(), context.consumers(), context.tickDelta(), context.camera()));
    }
}
