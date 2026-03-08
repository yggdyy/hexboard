package pub.pigeon.yggdyy.hexboard.content;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.interaction.staff.BoardStaffItem;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.LapisTypeblockItem;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.QuartzTypeblockItem;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.RedstoneTypeblockItem;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.WoolTypeblockItem;

public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(HexBoard.MOD_ID, Registries.ITEM);
    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(HexBoard.MOD_ID, Registries.CREATIVE_MODE_TAB);
    public static void register() {
        ITEMS.register();
        TABS.register();
    }
    public static final RegistrySupplier<CreativeModeTab> DEFAULT_TAB = TABS.register(
            "default",
            () -> CreativeTabRegistry.create(
                    Component.translatable("tab.hexboard.default"),
                    () -> new ItemStack(ModBlocks.BOARD_ITEM.get())
            )
    );
    public static Item.Properties defaultProperties() {
        return new Item.Properties().arch$tab(DEFAULT_TAB);
    }
    public static final RegistrySupplier<QuartzTypeblockItem> QUARTZ_TYPEBLOCK = ITEMS.register("quartz_typeblock", () -> new QuartzTypeblockItem(defaultProperties()));
    public static final RegistrySupplier<WoolTypeblockItem> WOOL_TYPEBLOCK = ITEMS.register("wool_typeblock", () -> new WoolTypeblockItem(defaultProperties()));
    public static final RegistrySupplier<LapisTypeblockItem> LAPIS_TYPEBLOCK = ITEMS.register("lapis_typeblock", () -> new LapisTypeblockItem(defaultProperties()));
    public static final RegistrySupplier<RedstoneTypeblockItem> REDSTONE_TYPEBLOCK = ITEMS.register("redstone_typeblock", () -> new RedstoneTypeblockItem(defaultProperties()));
    public static final RegistrySupplier<BoardStaffItem> BOARD_STAFF = ITEMS.register("board_staff", () -> new BoardStaffItem(defaultProperties().stacksTo(1)));
}
