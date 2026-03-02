package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.content.ModKeyMappings;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.ResolveOperation;

public class ResolveMode implements StaffModes.IMode {
    public static final Component DISPLAY = Component.translatable("staffmode.hexboard.resolve");
    public static final Component DISPLAY1 = Component.translatable("staffmode.hexboard.literal_resolve");
    public final boolean literal;
    public ResolveMode(boolean literal) {
        this.literal = literal;
    }
    @Override
    public void click() {
        if(Minecraft.getInstance().hitResult instanceof BlockHitResult hit) {
            BoardClient.updateSelect(hit);
        }
    }
    @Override
    public void shiftClick() {
        if(BoardClient.board != null) {
            ResolveOperation operation = new ResolveOperation(BoardClient.left, BoardClient.right, literal);
            if(operation.operate(BoardClient.board, Minecraft.getInstance().player, true) == Operation.OperateResult.SUCCESSFUL) {
                BoardClient.sendOperation(operation);
            }
        }
    }
    @Override
    public Component display() {
        return literal? DISPLAY1 : DISPLAY;
    }
    @Override
    public @Nullable KeyMapping getTriggerKey() {
        return literal? ModKeyMappings.STAFF_MODE_RESOLVE_LITERAL : ModKeyMappings.STAFF_MODE_RESOLVE;
    }
}
