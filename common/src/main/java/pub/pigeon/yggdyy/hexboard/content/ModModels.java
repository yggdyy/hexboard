package pub.pigeon.yggdyy.hexboard.content;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import pub.pigeon.yggdyy.hexboard.content.typeblock.renderers.QuartzTypeblockRenderer;
import pub.pigeon.yggdyy.hexboard.content.typeblock.renderers.WoolTypeblockRenderer;

import java.util.List;
import java.util.Map;

public class ModModels {
    public static final List<ResourceLocation> IDS = List.of(
            QuartzTypeblockRenderer.MODEL_ID,
            WoolTypeblockRenderer.BASE_ID,
            WoolTypeblockRenderer.OVERLAY_ID
    );
    public static Map<ResourceLocation, BakedModel> CUSTOMS = null;
    @NotNull
    public static BakedModel getCustomModel(ResourceLocation id) {
        BakedModel missing = Minecraft.getInstance().getModelManager().getMissingModel();
        return CUSTOMS == null ? missing : CUSTOMS.getOrDefault(id, missing);
    }
}
