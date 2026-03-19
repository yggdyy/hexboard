package pub.pigeon.yggdyy.hexboard.mixin;

import at.petrak.hexcasting.api.casting.eval.ResolvedPattern;
import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pub.pigeon.yggdyy.hexboard.content.interaction.staff.AddMode;

import java.util.List;

@Mixin(GuiSpellcasting.class)
public class GuiSpellCastingMixin {
    @Shadow(remap = false)
    private
    List<ResolvedPattern> patterns;
    @Inject(remap = false, method = "drawEnd", at = @At(value = "INVOKE", target = "Ljava/util/Set;addAll(Ljava/util/Collection;)Z", shift = At.Shift.AFTER, remap = false), cancellable = true)
    private void shieldBoardStaffSendPacket(CallbackInfoReturnable<Boolean> cir) {
        if(AddMode.screen != null && Minecraft.getInstance().screen == AddMode.screen && patterns.size() > 0) {
            AddMode.screen.onPatternAdd.accept(AddMode.screen, patterns.get(patterns.size() - 1).getPattern());
            cir.setReturnValue(false);
        }
    }
}
