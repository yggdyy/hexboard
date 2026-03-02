package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.content.ModKeyMappings;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.DeleteOperation;

import java.util.ArrayList;
import java.util.List;

public class DeleteMode implements StaffModes.IMode{
    public static final Component DISPLAY = Component.translatable("staffmode.hexboard.delete");
    @Override
    public void click() {
        if(Minecraft.getInstance().hitResult instanceof BlockHitResult hit) {
            BoardClient.updateSelect(hit);
        }
    }
    @Override
    public void shiftClick() {
        int left = Math.min(BoardClient.left, BoardClient.right);
        int right = Math.max(BoardClient.left, BoardClient.right);
        int target = BoardClient.target;
        if(left == -1 || right == -1 || target < left || target > right) {
            if(BoardClient.board != null && 0 <= target && BoardClient.board.slots.size() > target && !BoardClient.board.slots.get(target).getStack().isEmpty()) {
                DeleteOperation operation = new DeleteOperation(List.of(BoardClient.board.slots.get(target).getStack()), target);
                BoardClient.sendOperation(operation);
            }
            return;
        }
        if(BoardClient.board != null && right < BoardClient.board.slots.size()) {
            List<ItemStack> toDelete = new ArrayList<>();
            for (int i = left; i <= right; ++i) {
                toDelete.add(BoardClient.board.slots.get(i).getStack().copy());
            }
            DeleteOperation operation = new DeleteOperation(toDelete, left);
            BoardClient.sendOperation(operation);
            BoardClient.left = BoardClient.right = -1;
        }
    }
    @Override
    public Component display() {
        return DISPLAY;
    }
    @Override
    public @Nullable KeyMapping getTriggerKey() {
        return ModKeyMappings.STAFF_MODE_DELETE;
    }
}
