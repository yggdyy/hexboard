package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import at.petrak.hexcasting.common.items.storage.ItemFocus;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;

public class BoardStaffItem extends ItemFocus {
    public BoardStaffItem(Properties properties) {
        super(properties);
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(level.isClientSide) {
            if(player.isShiftKeyDown() || (BoardClient.staffMode.getTriggerKey() != null && BoardClient.staffMode.getTriggerKey().isDown())) {
                BoardClient.staffMode.shiftClick();
            } else {
                BoardClient.staffMode.click();
            }
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
    }
}
