package pub.pigeon.yggdyy.hexboard.fabric.mixin;

import dev.tizu.hexcessible.accessor.DrawStateMixinAccessor;
import dev.tizu.hexcessible.drawstate.DrawState;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pub.pigeon.yggdyy.hexboard.content.interaction.staff.SinglePatternScreen;

@Mixin(SinglePatternScreen.class)
public class SinglePatternScreenMixin$hexcessible{
    @Unique
    public DrawState state() {
        SinglePatternScreen self = (SinglePatternScreen) ((Object) this);
        DrawStateMixinAccessor castui = (DrawStateMixinAccessor) ((Object) self.hexGui);
        castui.disallowTyping();
        return castui.state();
    }
    @Inject(
            at = {@At("RETURN")},
            method = {"render"}
    )
    public void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        this.state().onRender(guiGraphics, i, j);
    }
}
