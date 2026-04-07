package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;
import pub.pigeon.yggdyy.hexboard.content.ModKeyMappings;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.MoveOperation;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation;

public class MoveMode implements StaffModes.IMode {
    public static final Component DISPLAY = Component.translatable("staffmode.hexboard.move");
    boolean doRender = false;
    int left, right;
    int color;
    @Override
    public void rightClick(InteractionHand hand) {
        if(Minecraft.getInstance().hitResult instanceof BlockHitResult hit) {
            BoardClient.updateSelect(hit);
        }
    }
    @Override
    public void leftClick(InteractionHand hand) {
        if(BoardClient.board != null) {
            if(BoardClient.left >= 0 && BoardClient.right >= 0) {
                MoveOperation operation = new MoveOperation(BoardClient.left, BoardClient.right, BoardClient.target);
                if (operation.operate(BoardClient.board, Minecraft.getInstance().player, true) == Operation.OperateResult.SUCCESSFUL) {
                    BoardClient.sendOperation(operation);
                    if (HexBoardConfig.config.resetSelectionAfterMove) {
                        BoardClient.left = BoardClient.right = -1;
                    } else {
                        BoardClient.left = operation.target;
                        BoardClient.right = operation.target + operation.right - operation.left;
                    }
                }
            } else if(BoardClient.target >= -1 && BoardClient.target < BoardClient.board.slots.size()) {
                int r = BoardClient.target;
                while(r < BoardClient.board.slots.size() && !BoardClient.board.slots.get(r).getStack().isEmpty()) ++r;
                --r;
                if(r >= BoardClient.target) {
                    MoveOperation operation = new MoveOperation(BoardClient.target, r, BoardClient.target + 1);
                    if(operation.operate(BoardClient.board, Minecraft.getInstance().player, true) == Operation.OperateResult.SUCCESSFUL) {
                        BoardClient.sendOperation(operation);
                    }
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
                doRender = false; //thread safety :(   (i don't quite know these, but there could be no wrong)
                left = t;
                right = t + r - l;
                boolean flag = true;
                for(int i = t; i <= t + r - l; ++i) {
                    if(l <= i && i <= r) continue;
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
        return ModKeyMappings.STAFF_MODE_MOVE;
    }
}
