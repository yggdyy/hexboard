package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;
import pub.pigeon.yggdyy.hexboard.content.ModKeyMappings;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.AddOperation;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation;

import java.util.ArrayList;
import java.util.List;

public class CopyMode implements StaffModes.IMode {
    public static final Component DISPLAY = Component.translatable("staffmode.hexboard.copy");
    boolean doRender = false;
    int left, right;
    int color;
    @Override
    public void click() {
        if(Minecraft.getInstance().hitResult instanceof BlockHitResult hit) {
            BoardClient.updateSelect(hit);
        }
    }
    @Override
    public void shiftClick() {
        if(BoardClient.board != null) {
            int l = Math.min(BoardClient.left, BoardClient.right), r = Math.max(BoardClient.left, BoardClient.right);
            int t = BoardClient.target;
            if (0 <= l && r < BoardClient.board.slots.size()) {
                List<ItemStack> toAdd = new ArrayList<>();
                for(int i = l; i <= r; ++i) {
                    toAdd.add(BoardClient.board.slots.get(i).getStack().copy());
                }
                AddOperation operation = new AddOperation(toAdd, t);
                if(operation.operate(BoardClient.board, Minecraft.getInstance().player, true) == Operation.OperateResult.SUCCESSFUL) {
                    BoardClient.sendOperation(operation);
                }
            }
        }
    }
    @Override
    public Component display() {
        return DISPLAY;
    }
    @Override
    public void tickCritical() {
        StaffModes.IMode.super.tickCritical();
        if(BoardClient.board != null) {
            int t = BoardClient.target, l = Math.min(BoardClient.left, BoardClient.right), r = Math.max(BoardClient.left, BoardClient.right), size = BoardClient.board.slots.size();
            if(0 <= t && t < size && 0 <= l && r < size && t + r - l < size) {
                doRender = false;
                left = t;
                right = t + r - l;
                boolean flag = true;
                for(int i = t; i <= t + r - l; ++i) {
                    if(!BoardClient.board.slots.get(i).getStack().isEmpty()) {
                        flag = false;
                        break;
                    }
                }
                color = 0x40_000000 | (flag ? HexBoardConfig.config.boardGoodColor : HexBoardConfig.config.boardBadColor);
                doRender = true;
                return;
            }
        }
        doRender = false;
    }
    @Override
    public void render(PoseStack stack, MultiBufferSource source, float tickDelta, Camera camera) {
        if(doRender) {
            BoardClient.renderOutline(stack, source, tickDelta, camera, left, right, color, 0.128);
        } else {
            StaffModes.IMode.super.render(stack, source, tickDelta, camera);
        }
    }
    @Override
    public @Nullable KeyMapping getTriggerKey() {
        return ModKeyMappings.STAFF_MODE_COPY;
    }
}
