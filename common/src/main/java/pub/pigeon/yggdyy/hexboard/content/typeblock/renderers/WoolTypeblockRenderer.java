package pub.pigeon.yggdyy.hexboard.content.typeblock.renderers;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.item.IotaHolderItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;
import pub.pigeon.yggdyy.hexboard.content.ModModels;
import pub.pigeon.yggdyy.hexboard.content.board.BoardBlockEntity;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.typeblock.TypeblockRenderers;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.WoolTypeblockItem;

public class WoolTypeblockRenderer implements TypeblockRenderers.IRenderer {
    public static final ResourceLocation BASE_ID = HexBoard.modLoc("typeblock/wool");
    public static final ResourceLocation OVERLAY_ID = HexBoard.modLoc("typeblock/wool_overlay");
    @Override
    public void renderOnBoard(ItemStack itemStack, BoardBlockEntity entity, float f, PoseStack stack, MultiBufferSource buffer, int light, int overlay, BlockEntityRendererProvider.Context context) {
        VertexConsumer consumer = buffer.getBuffer(ItemBlockRenderTypes.getRenderType(entity.getBlockState(), true));
        float r, g, b;
        r = g = b = 1F;
        if(itemStack.getItem() instanceof IotaHolderItem item) {
            CompoundTag tag = item.readIotaTag(itemStack);
            if(tag != null) {
                IotaType<?> type = IotaType.getTypeFromTag(tag);
                if(type != null) {
                    int color = type.color();
                    r = ((color >> 16) & 0xFF) / 255F;
                    g = ((color >> 8) & 0xFF) / 255F;
                    b = (color & 0xFF) / 255F;
                }
            }
        }
        stack.pushPose();
        context.getBlockRenderDispatcher().getModelRenderer().renderModel(stack.last(), consumer, entity.getBlockState(), ModModels.getCustomModel(BASE_ID), r, g, b, light, overlay);
        stack.translate(0, 0, 0.126);
        stack.scale(0.25F, 0.25F, 0.25F);
        context.getBlockRenderDispatcher().getModelRenderer().renderModel(stack.last(), consumer, entity.getBlockState(), ModModels.getCustomModel(OVERLAY_ID), r, g, b, light, overlay);
        stack.popPose();
    }
    @Override
    public void renderAsTarget(ItemStack itemStack, GuiGraphics graphics, float f) {
        WoolTypeblockItem item = (WoolTypeblockItem) itemStack.getItem();
        CompoundTag tag = item.readIotaTag(itemStack);
        if(tag != null) {
            graphics.drawString(Minecraft.getInstance().font, Component.translatable("item.hexboard.wool_typeblock").append(": ").append(IotaType.getDisplay(tag)), BoardClient.getHUDX(), BoardClient.getHUDY() + 2 * HexBoardConfig.config.hudLineHeight, 0xFF_000000 | HexBoardConfig.config.hudInfoWoolTypeblockColor);
        }
    }
}
