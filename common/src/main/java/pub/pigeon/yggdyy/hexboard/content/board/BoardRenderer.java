package pub.pigeon.yggdyy.hexboard.content.board;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.TypeblockItem;
import pub.pigeon.yggdyy.hexboard.content.typeblock.TypeblockRenderers;

public class BoardRenderer implements BlockEntityRenderer<BoardBlockEntity> {
    private final BlockEntityRendererProvider.Context context;
    public BoardRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }
    @Override
    public void render(BoardBlockEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        renderTypeblocks(blockEntity, f, poseStack, multiBufferSource, light, overlay);
        //renderEditInf(blockEntity, f, poseStack, multiBufferSource, light, overlay);
    }
    public void renderTypeblocks(BoardBlockEntity entity, float f, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {
        stack.pushPose();
        stack.translate(0.5, 0.5, 0.5);
        switch (entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
            case UP, DOWN, SOUTH:
                break;
            case NORTH:
                stack.mulPose(new Quaternionf().rotateY((float) Math.PI));
                break;
            case EAST:
                stack.mulPose(new Quaternionf().rotateY((float) Math.PI / 2F));
                break;
            case WEST:
                stack.mulPose(new Quaternionf().rotateY((float) -Math.PI / 2F));
                break;
        }
        stack.translate(-0.5, 0.25, 0);
        for(int i = 0; i < 4; ++i) {
            for(int j = 0; j < 4; ++j) {
                int idx = i * 4 + j;
                if(!entity.typeblocks.get(idx).isEmpty() && entity.typeblocks.get(idx).getItem() instanceof TypeblockItem item) {
                    if(TypeblockRenderers.REGISTRY.containsKey(item.getRendererID()) && TypeblockRenderers.REGISTRY.get(item.getRendererID()) != null) {
                        stack.pushPose();
                        stack.translate(j * 0.25, -i * 0.25, 0);
                        TypeblockRenderers.REGISTRY.get(item.getRendererID()).renderOnBoard(entity.typeblocks.get(idx), entity, f, stack, buffer, light, overlay, context);
                        stack.popPose();
                    }
                }
            }
        }
        stack.popPose();
    }
    public void renderEditInf(BoardBlockEntity entity, float f, PoseStack stack, MultiBufferSource buffer, int light, int overlay) {

    }
}
