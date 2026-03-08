package pub.pigeon.yggdyy.hexboard.content.typeblock.renderers;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.client.render.PatternColors;
import at.petrak.hexcasting.client.render.PatternRenderer;
import at.petrak.hexcasting.client.render.WorldlyPatternRenderHelpers;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;
import pub.pigeon.yggdyy.hexboard.content.ModIotaTypes;
import pub.pigeon.yggdyy.hexboard.content.ModModels;
import pub.pigeon.yggdyy.hexboard.content.board.BoardBlockEntity;
import pub.pigeon.yggdyy.hexboard.content.cast.iota.MacroDefineIota;
import pub.pigeon.yggdyy.hexboard.content.typeblock.TypeblockRenderers;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.LapisTypeblockItem;

import java.util.Objects;

public class LapisTypeblockRenderer implements TypeblockRenderers.IRenderer {
    public static final ResourceLocation MODEL_ID = HexBoard.modLoc("typeblock/lapis");
    @Override
    public void renderOnBoard(ItemStack itemStack, BoardBlockEntity entity, float f, PoseStack stack, MultiBufferSource buffer, int light, int overlay, BlockEntityRendererProvider.Context context) {
        VertexConsumer consumer = buffer.getBuffer(ItemBlockRenderTypes.getRenderType(entity.getBlockState(), true));
        context.getBlockRenderDispatcher().getModelRenderer().renderModel(stack.last(), consumer, entity.getBlockState(), ModModels.getCustomModel(MODEL_ID), 1f, 1f, 1f, light, overlay);
        if(!itemStack.isEmpty() && itemStack.getItem() instanceof LapisTypeblockItem item) {
            CompoundTag tag = item.readIotaTag(itemStack);
            if(tag != null && Objects.equals(IotaType.getTypeFromTag(tag), ModIotaTypes.MACRO_DEFINE)) {
                var data = tag.get(HexIotaTypes.KEY_DATA);
                if(data != null) {
                    HexPattern pattern = MacroDefineIota.deserialize(data).getPattern();
                    stack.translate(0, 0.25, 0.126);
                    stack.mulPose(new Quaternionf().rotateX((float) Math.PI));
                    stack.scale(0.25f, 0.25f, 0.25f);
                    PatternRenderer.renderPattern(pattern, stack, WorldlyPatternRenderHelpers.READABLE_SCROLL_SETTINGS, PatternColors.singleStroke(0xff_000000 | HexBoardConfig.config.lapisTypeblockPatternColor), 0, 128);
                }
            }
        }
    }
    @Override
    public void renderAsTarget(ItemStack itemStack, GuiGraphics graphics, float f) {

    }
}
