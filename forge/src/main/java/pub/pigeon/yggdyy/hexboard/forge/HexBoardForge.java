package pub.pigeon.yggdyy.hexboard.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import pub.pigeon.yggdyy.hexboard.HexBoardClient;
import pub.pigeon.yggdyy.hexboard.content.ModModels;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.staff.StaffModes;

import java.util.HashMap;

@Mod(HexBoard.MOD_ID)
public final class HexBoardForge {

    public HexBoardForge(FMLJavaModLoadingContext context) {
        EventBuses.registerModEventBus(HexBoard.MOD_ID, context.getModEventBus());
        HexBoard.init();
    }
    @Mod.EventBusSubscriber(modid = HexBoard.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            HexBoardClient.init();
            var eventBus = MinecraftForge.EVENT_BUS;
            eventBus.addListener((InputEvent.MouseScrollingEvent e) -> {
                boolean cancel = StaffModes.handleScroll(e.getScrollDelta());
                e.setCanceled(cancel);
            });
            eventBus.addListener((RenderLevelStageEvent e) -> {
                if(e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES)) {
                    BoardClient.renderWorldlyInfo(e.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource(), e.getPartialTick(), e.getCamera());
                }
            });
        }
        @SubscribeEvent
        public static void onModelRegister(ModelEvent.RegisterAdditional event) {
            ModModels.IDS.forEach(event::register);
        }
        @SubscribeEvent
        public static void onModelBaked(ModelEvent.BakingCompleted event) {
            if(ModModels.CUSTOMS == null) {
                ModModels.CUSTOMS = new HashMap<>();
            }
            event.getModels().entrySet().stream()
                    .filter(entry -> entry.getKey().getNamespace().equals(HexBoard.MOD_ID))
                    .forEach(entry -> ModModels.CUSTOMS.put(entry.getKey(), entry.getValue()));
        }
    }
}
