package pub.pigeon.yggdyy.hexboard.content.interaction;

import at.petrak.hexcasting.common.lib.HexAttributes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;
import pub.pigeon.yggdyy.hexboard.content.ModC2SHandlers;
import pub.pigeon.yggdyy.hexboard.content.board.BoardBlockEntity;
import pub.pigeon.yggdyy.hexboard.content.board.BoardInstance;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.AddOperation;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation;
import pub.pigeon.yggdyy.hexboard.content.interaction.staff.StaffModes;
import pub.pigeon.yggdyy.hexboard.content.typeblock.TypeblockRenderers;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.TypeblockItem;
import pub.pigeon.yggdyy.hexboard.mixin.UseOnContextAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardClient {
    public static void init() {
        genSlotAreas();
    }
    public static BoardInstance board = null;
    public static int target = -1;
    public static int left = -1;
    public static int right = -1;
    @FunctionalInterface
    public interface IIdxHandler {
        void handle(int newIdx);
    }
    public static final IIdxHandler handleTarget = newIdx -> target = newIdx;
    public static final Map<Direction, List<AABB>> slotAreas = new HashMap<>();
    public static int timer = 0, interval = 10;
    public static StaffModes.IMode staffMode = StaffModes.MODES.get(0);
    public static boolean doRenderHUD = false;
    public static void reset() {
        board = null;
        target = left = right = -1;
    }
    public static void genSlotAreas() {
        for(var d : Direction.values()) {
            if(d != Direction.UP && d != Direction.DOWN) {
                Vec3 f = Vec3.atLowerCornerOf(d.getNormal());
                Vec3 y = new Vec3(0, -1, 0);
                Vec3 x = f.cross(y);
                AABB origin = new AABB(Vec3.ZERO.add(f.scale(-0.1)), x.scale(0.25).add(y.scale(0.25)).add(f.scale(0.1))).move(x.scale(-0.5).add(y.scale(-0.5)));
                List<AABB> list = new ArrayList<>();
                for(int i = 0; i < 4; ++i) {
                    for(int j = 0; j < 4; ++j) {
                        AABB aabb = origin.move(x.scale(0.25 * j).add(y.scale(0.25 * i)));
                        list.add(aabb);
                    }
                }
                slotAreas.put(d, list);
            }
        }
    }
    @SuppressWarnings("unchecked")
    public static void sendOperation(Operation operation) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeResourceLocation(operation.getType().ID);
        buf.writeBlockPos(board.mainEntity.getBlockPos());
        buf.writeWithCodec(NbtOps.INSTANCE, (Codec<Operation>) operation.getType().CODEC, operation);
        NetworkManager.sendToServer(ModC2SHandlers.OPERATION, buf);
    }
    public static boolean update(BlockHitResult hit, IIdxHandler handler) {
        BlockPos blockPos = hit.getBlockPos();
        Level level = Minecraft.getInstance().level;
        if(level != null && level.getBlockEntity(blockPos) != null && level.getBlockEntity(blockPos) instanceof BoardBlockEntity entity) {
            List<AABB> slots = slotAreas.getOrDefault(entity.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING), List.of());
            Vec3 relatePos = hit.getLocation().subtract(blockPos.getCenter());
            int local = -1;
            for(int i = 0; i < slots.size(); ++i) {
                if(slots.get(i).contains(relatePos)) {
                    local = i;
                    break;
                }
            }
            if(0 <= local && local < 16) {
                BoardInstance newBoard = BoardInstance.create(level, blockPos);
                if(board == null || !board.equals(newBoard)) {
                    board = newBoard;
                    target = left = right = -1;
                }
                if(board != null) {
                    int idx = board.findGlobalIdx(entity, local);
                    handler.handle(idx);
                    return true;
                }
            }
        }
        target = -1;
        return false;
    }
    public static boolean updateTarget(BlockHitResult hit) {
        return update(hit, handleTarget);
    }
    public static boolean updateSelect(BlockHitResult hit) {
        return update(hit, idx -> {
            right = left;
            left = idx;
        });
    }
    public static InteractionResult useWithTypeblockItem(UseOnContext useOnContext) {
        if(updateTarget(((UseOnContextAccessor)useOnContext).hexboard$getHitResult())) {
            if (board != null && target != -1) {
                AddOperation operation = new AddOperation(List.of(useOnContext.getItemInHand().copyWithCount(1)), target);
                if(operation.operate(board, useOnContext.getPlayer(), true) == Operation.OperateResult.SUCCESSFUL) {
                    sendOperation(operation);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
    public static void tick(Level level) {
        ++timer;
        if(timer >= interval) {
            timer = 0;
            tickCritical(level);
        }
    }
    public static void tickCritical(Level level) {
        doRenderHUD = Minecraft.getInstance().player != null && Minecraft.getInstance().player.getAttributeValue(HexAttributes.SCRY_SIGHT) > 0;
        staffMode.tickCritical();
        if(Minecraft.getInstance().hitResult instanceof BlockHitResult hit) {
            if(updateTarget(hit)) {
                interval = 2;
                return;
            }
        } else {
            target = -1;
        }
        interval = 10;
    }
    public static int getHUDX() {
        return HexBoardConfig.config.hudInfoX;
    }
    public static int getHUDY() {
        return HexBoardConfig.config.hudInfoY;
    }
    public static void renderHUDInfo(GuiGraphics graphics, float tickDelta) {
        if(doRenderHUD && board != null) {
            if (target >= 0 && target < board.slots.size() && Minecraft.getInstance().screen == null) {
                int x = getHUDX(), y = getHUDY(), c1 = HexBoardConfig.config.hudInfoStaffModeColor | 0xFF_000000, c2 = HexBoardConfig.config.hudInfoTargetIndexColor | 0xFF_000000;
                graphics.drawString(Minecraft.getInstance().font, Component.translatable("info.hexboard.staff_mode").append(staffMode.display()), x, y, c1);
                graphics.drawString(Minecraft.getInstance().font, Component.translatable("info.hexboard.target_index", target), x, y + HexBoardConfig.config.hudLineHeight, c2);
                ItemStack itemStack = board.slots.get(target).getStack();
                if (!itemStack.isEmpty() && itemStack.getItem() instanceof TypeblockItem item) {
                    TypeblockRenderers.IRenderer renderer = TypeblockRenderers.REGISTRY.get(item.getRendererID());
                    if (renderer != null) {
                        renderer.renderAsTarget(itemStack, graphics, tickDelta);
                    }
                }
            }
        }
    }
    public static void renderWorldlyInfo(PoseStack stack, MultiBufferSource bufferSource, float tickDelta, Camera camera) {
        try {
            if (board != null) {
                if (left != -1 && right != -1) {
                    int l = Math.min(left, right), r = Math.max(left, right);
                    if (0 <= l && r < board.slots.size()) {
                        renderOutline(stack, bufferSource, tickDelta, camera, l, r, 0x40_000000 | HexBoardConfig.config.boardSectionColor);
                    }
                }
                staffMode.render(stack, bufferSource, tickDelta, camera);
                if (bufferSource instanceof MultiBufferSource.BufferSource source) {
                    source.endBatch();
                }
            }
        } catch (Exception ignored) {

        }
    }
    public static void renderOutline(PoseStack stack, MultiBufferSource source, float tickDelta, Camera camera, int l, int r, int color) {
        renderOutline(stack, source, tickDelta, camera, l, r, color, 0.127);
    }
    public static void renderOutline(PoseStack stack, MultiBufferSource source, float tickDelta, Camera camera, int l, int r, int color, double z) {
        int first = l, second = l;
        while(true) {
            while (second <= r && board.slots.get(second).local / 4 == board.slots.get(first).local / 4) ++second;
            renderSingleOutline(stack, source, tickDelta, camera, first, second - 1, color, z);
            if(second > r) break;
            first = second;
        }
    }
    public static void renderSingleOutline(PoseStack stack, MultiBufferSource source, float tickDelta, Camera camera, int l, int r, int color) {
        renderSingleOutline(stack, source, tickDelta, camera, l, r, color, 0.127);
    }
    public static void renderSingleOutline(PoseStack stack, MultiBufferSource source, float tickDelta, Camera camera, int l, int r, int color, double z) {
        stack.pushPose();
        BoardBlockEntity e1 = board.slots.get(l).entity, e2 = board.slots.get(r).entity;
        int i1 = board.slots.get(l).local, i2 = board.slots.get(r).local;
        double len = e1.getBlockPos().distManhattan(e2.getBlockPos()) + (i2 % 4 - i1 % 4 + 1) * 0.25;
        Vec3 center1 = e1.getBlockPos().getCenter();
        stack.translate(center1.x, center1.y, center1.z);
        Vec3 center2 = camera.getPosition();
        stack.translate(-center2.x, -center2.y, -center2.z);
        switch (e1.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)) {
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
        };
        stack.translate(-0.5, 0.25, z);
        stack.translate(i1 % 4 * 0.25, -((i1 / 4) * 0.25), 0);
        VertexConsumer consumer = source.getBuffer(RenderType.debugQuads());
        Matrix4f mat = stack.last().pose();
        consumer.vertex(mat, 0, 0, 0).color(color).endVertex();
        consumer.vertex(mat, (float) len, 0, 0).color(color).endVertex();
        consumer.vertex(mat, (float) len, 0.25F, 0).color(color).endVertex();
        consumer.vertex(mat, 0, 0.25F, 0).color(color).endVertex();
        stack.popPose();
    }
}
