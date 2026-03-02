package pub.pigeon.yggdyy.hexboard.content;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.board.BoardBlock;
import pub.pigeon.yggdyy.hexboard.content.board.BoardBlockEntity;

import java.util.Set;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(HexBoard.MOD_ID, Registries.BLOCK);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(HexBoard.MOD_ID, Registries.ITEM);
    private static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(HexBoard.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static void register() {
        BLOCKS.register();
        ITEMS.register();
        ENTITIES.register();
    }
    public static final RegistrySupplier<BoardBlock> BOARD = BLOCKS.register("board", () -> new BoardBlock(BlockBehaviour.Properties.copy(Blocks.BASALT)));
    public static final RegistrySupplier<BlockItem> BOARD_ITEM = ITEMS.register("board", () -> new BlockItem(BOARD.get(), new Item.Properties().arch$tab(ModItems.DEFAULT_TAB)));
    public static final RegistrySupplier<BlockEntityType<BoardBlockEntity>> BOARD_ENTITY = ENTITIES.register("board", () -> BlockEntityType.Builder.of(BoardBlockEntity::new, BOARD.get()).build(null));
}
