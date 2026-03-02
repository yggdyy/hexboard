package pub.pigeon.yggdyy.hexboard.mixin;

import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.interop.patchouli.AbstractPatternComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import pub.pigeon.yggdyy.hexboard.content.ModItems;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.AddOperation;
import pub.pigeon.yggdyy.hexboard.content.interaction.operation.Operation;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;

import java.util.List;

@Mixin(value = AbstractPatternComponent.class, remap = false)
public abstract class AbstractPatternComponentMixin implements ICustomComponent{
    @Shadow protected transient int x;
    @Shadow protected transient int y;
    @Shadow private transient List<HexPattern> patterns;
    @Shadow public abstract boolean showStrokeOrder();
    /*
    I think no one else will mixin this method like me. Hope I am right.
     */
    @Override
    public boolean mouseClicked(IComponentRenderContext context, double mouseX, double mouseY, int mouseButton) {
        if((Screen.hasControlDown() || Screen.hasAltDown() || Screen.hasShiftDown()) && showStrokeOrder()) {
            if(context.isAreaHovered((int) mouseX, (int) mouseY, x - 58, y - 32, 116, 64)) {
                if(patterns != null && 0 <= mouseButton && mouseButton < patterns.size()) {
                    if(BoardClient.board != null) {
                        HexPattern pat = patterns.get(mouseButton);
                        PatternIota iota = new PatternIota(pat);
                        ItemStack stack = new ItemStack(ModItems.QUARTZ_TYPEBLOCK.get());
                        ModItems.QUARTZ_TYPEBLOCK.get().writeDatum(stack, iota);
                        AddOperation operation = new AddOperation(List.of(stack), BoardClient.target);
                        if(operation.operate(BoardClient.board, Minecraft.getInstance().player, true) == Operation.OperateResult.SUCCESSFUL) {
                            BoardClient.sendOperation(operation);
                            Minecraft.getInstance().setScreen(null);
                            return true;
                        }
                    }
                }
            }
        }
        return ICustomComponent.super.mouseClicked(context, mouseX, mouseY, mouseButton);
    }
}
