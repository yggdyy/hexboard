package pub.pigeon.yggdyy.hexboard;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import pub.pigeon.yggdyy.hexboard.content.ModBlocks;
import pub.pigeon.yggdyy.hexboard.content.ModKeyMappings;
import pub.pigeon.yggdyy.hexboard.content.board.BoardRenderer;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.staff.StaffModes;
import pub.pigeon.yggdyy.hexboard.content.typeblock.TypeblockRenderers;

public class HexBoardClient {
    public static void init() {
        BlockEntityRendererRegistry.register(ModBlocks.BOARD_ENTITY.get(), BoardRenderer::new);
        TypeblockRenderers.init();
        StaffModes.init();
        BoardClient.init();
        ModKeyMappings.init();
        ClientTickEvent.ClientLevel.CLIENT_LEVEL_POST.register(BoardClient::tick);
        ClientTickEvent.Client.CLIENT_POST.register(StaffModes::handleModeChange);
        ClientGuiEvent.RENDER_HUD.register(BoardClient::renderHUDInfo);
    }
}
