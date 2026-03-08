package pub.pigeon.yggdyy.hexboard.content.typeblock;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import pub.pigeon.yggdyy.hexboard.content.board.BoardBlockEntity;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.LapisTypeblockItem;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.QuartzTypeblockItem;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.RedstoneTypeblockItem;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.WoolTypeblockItem;
import pub.pigeon.yggdyy.hexboard.content.typeblock.renderers.LapisTypeblockRenderer;
import pub.pigeon.yggdyy.hexboard.content.typeblock.renderers.QuartzTypeblockRenderer;
import pub.pigeon.yggdyy.hexboard.content.typeblock.renderers.RedstoneTypeblockRenderer;
import pub.pigeon.yggdyy.hexboard.content.typeblock.renderers.WoolTypeblockRenderer;

import java.util.HashMap;
import java.util.Map;

public class TypeblockRenderers {
    public interface IRenderer {
        void renderOnBoard(ItemStack itemStack, BoardBlockEntity entity, float f, PoseStack stack, MultiBufferSource buffer, int light, int overlay, BlockEntityRendererProvider.Context context);
        void renderAsTarget(ItemStack itemStack, GuiGraphics graphics, float f);
    }
    public static final Map<ResourceLocation, IRenderer> REGISTRY = new HashMap<>();
    public static void register(ResourceLocation id, IRenderer renderer) {
        if(!REGISTRY.containsKey(id)) {
            REGISTRY.put(id, renderer);
        }
    }
    public static void init() {
        register(QuartzTypeblockItem.ID, new QuartzTypeblockRenderer());
        register(WoolTypeblockItem.ID, new WoolTypeblockRenderer());
        register(LapisTypeblockItem.ID, new LapisTypeblockRenderer());
        register(RedstoneTypeblockItem.ID, new RedstoneTypeblockRenderer());
    }
}
