package pub.pigeon.yggdyy.hexboard.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemStackUtil {
    public static List<ItemStack> getAllItemStacks(Inventory inventory) {
        List<ItemStack> res = new ArrayList<>();
        res.addAll(inventory.offhand);
        res.addAll(inventory.items);
        res.addAll(inventory.armor);
        return res;
    }
    public static List<ItemStack> copy(List<ItemStack> stacks) {
        List<ItemStack> res = new ArrayList<>();
        for (ItemStack stack : stacks) {
            res.add(stack.copy());
        }
        return res;
    }
    public static boolean compare(ItemStack s1, ItemStack s2) {
        if(s1.isEmpty() && s2.isEmpty()) {
            return true;
        }
        if(s1.getItem() == s2.getItem()) {
            if(!s1.hasTag() && !s2.hasTag()) {
                return true;
            }
            return Objects.equals(s1.getTag(), s2.getTag());
        }
        return false;
    }
    public static boolean compareIgnoreNbt(ItemStack s1, ItemStack s2) {
        if(s1.isEmpty() && s2.isEmpty()) {
            return true;
        }
        return s1.getItem() == s2.getItem();
    }
    //return needCount - actuallyCount
    public static int consume(List<ItemStack> stacks, ItemStack need) {
        if(need.isEmpty()) return 0;
        for(ItemStack stack : stacks) {
            if(compare(stack, need)) {
                int c = Math.min(stack.getCount(), need.getCount());
                stack.shrink(c);
                need.shrink(c);
                if(need.isEmpty()) break;
            }
        }
        return need.getCount();
    }
    public static int consumeIgnoreNbt(List<ItemStack> stacks, ItemStack need) {
        if(need.isEmpty()) return 0;
        for(ItemStack stack : stacks) {
            if(compareIgnoreNbt(stack, need)) {
                int c = Math.min(stack.getCount(), need.getCount());
                stack.shrink(c);
                need.shrink(c);
                if(need.isEmpty()) break;
            }
        }
        return need.getCount();
    }
    //return how many are left
    public static int simulateAdd(List<ItemStack> stacks, ItemStack give) {
        if(give.isEmpty()) return 0;
        for(int i = 0; i < stacks.size(); ++i) {
            ItemStack stack = stacks.get(i);
            if(stack.isEmpty() || compare(stack, give)) {
                int g = Math.min(stack.getMaxStackSize() - stack.getCount(), give.getCount());
                if(stack.isEmpty()) stacks.set(i, give.copyWithCount(g));
                else stack.grow(g);
                give.shrink(g);
                if(give.isEmpty()) break;
            }
        }
        return give.getCount();
    }
    //a slightly different ContainerHelper.saveAllItems to ensure network update
    public static void saveAllItems(CompoundTag compoundTag, NonNullList<ItemStack> nonNullList, boolean bl) {
        ListTag listTag = new ListTag();
        for (int i = 0; i < nonNullList.size(); i++) {
            ItemStack itemStack = nonNullList.get(i);
            CompoundTag compoundTag2 = new CompoundTag();
            compoundTag2.putByte("Slot", (byte)i);
            itemStack.save(compoundTag2);
            listTag.add(compoundTag2);
        }
        if (!listTag.isEmpty() || bl) {
            compoundTag.put("Items", listTag);
        }
    }
    public static boolean isPlayerHas(Player player, ItemStack stack) {
        for(ItemStack now : getAllItemStacks(player.getInventory())) {
            if(compare(now, stack) && now.getCount() >= stack.getCount()) {
                return true;
            }
        }
        return false;
    }
}
