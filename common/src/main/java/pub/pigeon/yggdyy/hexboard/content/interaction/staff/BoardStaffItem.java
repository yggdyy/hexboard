package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import at.petrak.hexcasting.common.items.storage.ItemFocus;
import dev.architectury.event.EventResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.content.ModBlocks;
import pub.pigeon.yggdyy.hexboard.content.ModItems;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;

public class BoardStaffItem extends ItemFocus {
    public BoardStaffItem(Properties properties) {
        super(properties);
    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if(level.isClientSide) {
            if(player.isShiftKeyDown() || (BoardClient.staffMode.getTriggerKey() != null && BoardClient.staffMode.getTriggerKey().isDown())) {
                BoardClient.staffMode.shiftRightClick(interactionHand);
            } else {
                BoardClient.staffMode.rightClick(interactionHand);
            }
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
    }
    public static EventResult attack(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        Level level = player.level();
        if(player.getItemInHand(hand).is(ModItems.BOARD_STAFF.get()) && level.getBlockState(pos).is(ModBlocks.BOARD.get())) {
            if(level.isClientSide) {
                if (player.isShiftKeyDown() || (BoardClient.staffMode.getTriggerKey() != null && BoardClient.staffMode.getTriggerKey().isDown())) {
                    BoardClient.staffMode.shiftLeftClick(hand);
                } else {
                    BoardClient.staffMode.leftClick(hand);
                }
            }
            return EventResult.interrupt(false);
        }
        return EventResult.pass();
    }
}