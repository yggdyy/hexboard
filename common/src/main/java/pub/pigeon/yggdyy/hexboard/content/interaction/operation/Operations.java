package pub.pigeon.yggdyy.hexboard.content.interaction.operation;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Operations {
    public static final Map<ResourceLocation, Operation.Type<?>> REGISTRY = new HashMap<>();
    public static void register(Operation.Type<?> type) {
        if(!REGISTRY.containsKey(type.ID)) {
            REGISTRY.put(type.ID, type);
        }
    }
    public static void init() {
        register(AddOperation.TYPE);
        register(DeleteOperation.TYPE);
        register(MoveOperation.TYPE);
        register(ResolveOperation.TYPE);
        register(LoadOperation.TYPE);
    }
}
