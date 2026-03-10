package pub.pigeon.yggdyy.hexboard.content;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.content.cast.operation.OpDeleteGambit;
import pub.pigeon.yggdyy.hexboard.content.cast.operation.OpLoadGambit;
import pub.pigeon.yggdyy.hexboard.content.cast.operation.OpMoveGambit;
import pub.pigeon.yggdyy.hexboard.content.cast.operation.OpResolveGambit;

public class ModPatterns {
    private static ActionRegistryEntry register(ResourceLocation id, ActionRegistryEntry entry) {
        Registry.register(HexActions.REGISTRY, id, entry);
        return entry;
    }
    public static final ActionRegistryEntry LOAD = register(HexBoard.modLoc("load"), new ActionRegistryEntry(HexPattern.fromAngles("wqawqwewdewea", HexDir.NORTH_EAST), new OpLoadGambit()));
    public static final ActionRegistryEntry DELETE = register(HexBoard.modLoc("delete"), new ActionRegistryEntry(HexPattern.fromAngles("wqawqwewdewed", HexDir.NORTH_EAST), new OpDeleteGambit()));
    public static final ActionRegistryEntry RESOLVE = register(HexBoard.modLoc("resolve"), new ActionRegistryEntry(HexPattern.fromAngles("wqawqwewdeweq", HexDir.NORTH_EAST), new OpResolveGambit(false)));
    public static final ActionRegistryEntry LITERAL_RESOLVE = register(HexBoard.modLoc("literal_resolve"), new ActionRegistryEntry(HexPattern.fromAngles("wqawqwewdewee", HexDir.NORTH_EAST), new OpResolveGambit(true)));
    public static final ActionRegistryEntry MOVE = register(HexBoard.modLoc("move"), new ActionRegistryEntry(HexPattern.fromAngles("wqawqwewdewew", HexDir.NORTH_EAST), new OpMoveGambit()));
    public static void init() {

    }
}
