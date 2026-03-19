package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SinglePatternScreen extends Screen {
    public final BiConsumer<SinglePatternScreen, HexPattern> onPatternAdd;
    public final Consumer<SinglePatternScreen> onScreenClose;
    public final GuiSpellcasting hexGui;
    public SinglePatternScreen(BiConsumer<SinglePatternScreen, HexPattern> onPatternAdd, Consumer<SinglePatternScreen> onScreenClose, InteractionHand hand) {
        super(Component.translatable("screen.hexboard.single_pattern"));
        this.onPatternAdd = onPatternAdd;
        this.onScreenClose = onScreenClose;
        hexGui = new GuiSpellcasting(hand, new ArrayList<>(), new ArrayList<>(), null, 0);
    }
    @Override
    protected void init() {
        super.init();
        hexGui.init(Minecraft.getInstance(), this.width, this.height);
    }
    @Override
    public boolean mouseClicked(double x, double y, int i) {
        if(super.mouseClicked(x, y, i)) {
            return true;
        }
        return hexGui.mouseClicked(x, y, i);
    }
    @Override
    public void mouseMoved(double x, double y) {
        super.mouseMoved(x, y);
        hexGui.mouseMoved(x, y);
    }
    @Override
    public boolean mouseDragged(double x, double y, int i, double dx, double dy) {
        if(super.mouseDragged(x, y, i, dx, dy)) {
            return true;
        }
        return hexGui.mouseDragged(x, y, i, dx, dy);
    }
    @Override
    public boolean mouseReleased(double x, double y, int i) {
        if(super.mouseReleased(x, y, i)) {
            return true;
        }
        return hexGui.mouseReleased(x, y, i);
    }
    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        hexGui.render(guiGraphics, i, j, f);
    }
    @Override
    public void tick() {
        super.tick();
        hexGui.tick();
    }
    @Override
    public void onClose() {
        hexGui.onClose();
        onScreenClose.accept(this);
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
