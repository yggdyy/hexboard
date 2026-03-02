package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import at.petrak.hexcasting.api.casting.iota.PatternIota;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.content.ModItems;
import pub.pigeon.yggdyy.hexboard.content.ModKeyMappings;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.AddOperation;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation;

import java.util.List;

public class AddMode implements StaffModes.IMode{
    public static final Component DISPLAY = Component.translatable("staffmode.hexboard.add");
    @Override
    public void click() {
        if(BoardClient.target > -1 && BoardClient.board != null) {
            Minecraft.getInstance().setScreen(new SinglePatternScreen(pat -> {
                ItemStack stack = new ItemStack(ModItems.QUARTZ_TYPEBLOCK.get());
                ModItems.QUARTZ_TYPEBLOCK.get().writeDatum(stack, new PatternIota(pat));
                AddOperation operation = new AddOperation(List.of(stack), BoardClient.target);
                if(operation.operate(BoardClient.board, Minecraft.getInstance().player, true) == Operation.OperateResult.SUCCESSFUL) {
                    BoardClient.sendOperation(operation);
                }
                Minecraft.getInstance().setScreen(null);
            }));
        }
    }
    @Override
    public void shiftClick() {
        click();
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
