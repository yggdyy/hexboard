package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.content.ModKeyMappings;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.LoadOperation;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation;

public class LoadMode implements StaffModes.IMode {
    public static final Component DISPLAY = Component.translatable("staffmode.hexboard.load");
    @Override
    public void click() {

    }
    @Override
    public void shiftClick() {
        if(BoardClient.board != null) {
            LoadOperation operation = new LoadOperation(BoardClient.target);
            if(operation.operate(BoardClient.board, Minecraft.getInstance().player, true) == Operation.OperateResult.SUCCESSFUL) {
                BoardClient.sendOperation(operation);
            }
        }
    }
    @Override
    public Component display() {
        return DISPLAY;
    }
    @Override
    public @Nullable KeyMapping getTriggerKey() {
        return ModKeyMappings.STAFF_MODE_LOAD;
    }
}
