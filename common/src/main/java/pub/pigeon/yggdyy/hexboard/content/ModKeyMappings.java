package pub.pigeon.yggdyy.hexboard.content;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;

public class ModKeyMappings {
    public static final KeyMapping REFER_PATCHOULI = new KeyMapping(
            "key.hexboard.refer_patchouli",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_R,
            "category.hexboard.default"
    );
    public static final KeyMapping STAFF_MODE_ADD = new KeyMapping(
            "key.hexboard.staffmodeadd",
            InputConstants.Type.KEYSYM,
            -1,
            "category.hexboard.default"
    );
    public static final KeyMapping STAFF_MODE_COPY = new KeyMapping(
            "key.hexboard.staffmodecopy",
            InputConstants.Type.KEYSYM,
            -1,
            "category.hexboard.default"
    );
    public static final KeyMapping STAFF_MODE_DELETE = new KeyMapping(
            "key.hexboard.staffmodedelete",
            InputConstants.Type.KEYSYM,
            -1,
            "category.hexboard.default"
    );
    public static final KeyMapping STAFF_MODE_LOAD = new KeyMapping(
            "key.hexboard.staffmodeload",
            InputConstants.Type.KEYSYM,
            -1,
            "category.hexboard.default"
    );
    public static final KeyMapping STAFF_MODE_MOVE = new KeyMapping(
            "key.hexboard.staffmodemove",
            InputConstants.Type.KEYSYM,
            -1,
            "category.hexboard.default"
    );
    public static final KeyMapping STAFF_MODE_RESOLVE = new KeyMapping(
            "key.hexboard.staffmoderesolve",
            InputConstants.Type.KEYSYM,
            -1,
            "category.hexboard.default"
    );
    public static final KeyMapping STAFF_MODE_RESOLVE_LITERAL = new KeyMapping(
            "key.hexboard.staffmoderesolveliteral",
            InputConstants.Type.KEYSYM,
            -1,
            "category.hexboard.default"
    );
    public static void init() {
        KeyMappingRegistry.register(REFER_PATCHOULI);
        KeyMappingRegistry.register(STAFF_MODE_ADD);
        KeyMappingRegistry.register(STAFF_MODE_COPY);
        KeyMappingRegistry.register(STAFF_MODE_DELETE);
        KeyMappingRegistry.register(STAFF_MODE_LOAD);
        KeyMappingRegistry.register(STAFF_MODE_MOVE);
        KeyMappingRegistry.register(STAFF_MODE_RESOLVE);
        KeyMappingRegistry.register(STAFF_MODE_RESOLVE_LITERAL);
    }
}
