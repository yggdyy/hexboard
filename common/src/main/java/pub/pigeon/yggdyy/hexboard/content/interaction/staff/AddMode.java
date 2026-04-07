package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import at.petrak.hexcasting.api.casting.iota.PatternIota;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.content.ModItems;
import pub.pigeon.yggdyy.hexboard.content.ModKeyMappings;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.AddOperation;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.ModifyOperation;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation;

import java.util.List;

public class AddMode implements StaffModes.IMode{
    @Nullable
    public static SinglePatternScreen screen = null;
    public static final Component DISPLAY = Component.translatable("staffmode.hexboard.add");
    @Override
    public void rightClick(InteractionHand hand) {
        if(BoardClient.board != null && BoardClient.target > -1 && BoardClient.target < BoardClient.board.slots.size()) {
            if(screen == null) {
                screen = new SinglePatternScreen((scr, pat) -> {
                    if(BoardClient.board != null && BoardClient.target > -1 && BoardClient.target < BoardClient.board.slots.size()) {
                        ItemStack toDelete = BoardClient.board.slots.get(BoardClient.target).getStack().copy();
                        ItemStack toAdd = new ItemStack(ModItems.QUARTZ_TYPEBLOCK.get());
                        ModItems.QUARTZ_TYPEBLOCK.get().writeDatum(toAdd, new PatternIota(pat));
                        if(toDelete.isEmpty()) {
                            AddOperation operation = new AddOperation(List.of(toAdd), BoardClient.target);
                            if (operation.operate(BoardClient.board, Minecraft.getInstance().player, true) == Operation.OperateResult.SUCCESSFUL) {
                                BoardClient.sendOperation(operation);
                            }
                        } else {
                            ModifyOperation operation = new ModifyOperation(toDelete, toAdd, BoardClient.target);
                            if(operation.operate(BoardClient.board, Minecraft.getInstance().player, true) == Operation.OperateResult.SUCCESSFUL) {
                                BoardClient.sendOperation(operation);
                            }
                        }
                    }
                    scr.onClose();
                }, scr -> {
                    screen = null;
                }, hand);
            }
            Minecraft.getInstance().setScreen(screen);
        }
    }
    @Override
    public void leftClick(InteractionHand hand) {
        rightClick(hand);
    }
    @Override
    public Component display() {
        return DISPLAY;
    }
    @Override
    public @Nullable KeyMapping getTriggerKey() {
        return ModKeyMappings.STAFF_MODE_ADD;
    }
}
