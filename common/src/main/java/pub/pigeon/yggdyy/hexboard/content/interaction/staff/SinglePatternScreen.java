package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import at.petrak.hexcasting.api.casting.math.HexAngle;
import at.petrak.hexcasting.api.casting.math.HexCoord;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.mod.HexConfig;
import at.petrak.hexcasting.client.render.RenderLib;
import at.petrak.hexcasting.common.lib.HexAttributes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class SinglePatternScreen extends Screen {
    public final Consumer<HexPattern> callback;
    public PatternDrawState drawState = new PatternDrawState.BetweenPatterns();
    public SinglePatternScreen(Consumer<HexPattern> callback) {
        super(Component.translatable("screen.hexboard.single_pattern"));
        this.callback = callback;
    }
    public boolean drawStart(double x, double y) {
        double mx = Mth.clamp(x, 0.0, width);
        double my = Mth.clamp(y, 0.0, height);
        if(drawState instanceof PatternDrawState.BetweenPatterns) {
            HexCoord coord = pxToCoord(new Vec2((float) mx, (float) my));
            drawState = new PatternDrawState.JustStarted(coord);
        }
        return false;
    }
    public boolean drawEnd() {
        if(drawState instanceof PatternDrawState.JustStarted) {
            drawState = new PatternDrawState.BetweenPatterns();
        } else if(drawState instanceof PatternDrawState.Drawing drawing) {
            HexPattern pat = drawing.wipPattern;
            drawState = new PatternDrawState.BetweenPatterns();
            callback.accept(pat);
        }
        return false;
    }
    public boolean drawMove(double x, double y) {
        double mx = Mth.clamp(x, 0.0, width);
        double my = Mth.clamp(y, 0.0, height);
        HexCoord anchorCoord = null;
        if(drawState instanceof PatternDrawState.JustStarted justStarted) {
            anchorCoord = justStarted.start;
        } else if(drawState instanceof PatternDrawState.Drawing drawing) {
            anchorCoord = drawing.current;
        }
        if(anchorCoord != null) {
            Vec2 anchor = coordToPx(anchorCoord);
            Vec2 mouse = new Vec2((float) mx, (float) my);
            double snapDist = hexSize() * hexSize() * 2.0 * Mth.clamp(HexConfig.client().gridSnapThreshold(), 0.5, 1.0);
            if(anchor.distanceToSqr(mouse) >= snapDist) {
                Vec2 delta = mouse.add(anchor.negated());
                double angle = Math.atan2(delta.y, delta.x);
                float snappedAngle = (float) (angle / Mth.TWO_PI % 6.0F);
                HexDir newdir = HexDir.values()[(Math.round(snappedAngle * 6F) + 7) % 6];
                HexCoord idealNextLoc = anchorCoord.plus(newdir);
                if(drawState instanceof PatternDrawState.JustStarted) {
                    HexPattern pat = new HexPattern(newdir, new ArrayList<>());
                    drawState = new PatternDrawState.Drawing(anchorCoord, idealNextLoc, pat);
                } else if(drawState instanceof PatternDrawState.Drawing ds) {
                    HexDir lastDir = ds.wipPattern.finalDir();
                    if(newdir == lastDir.rotatedBy(HexAngle.BACK)) {
                        if(ds.wipPattern.getAngles().isEmpty()) {
                            drawState = new PatternDrawState.JustStarted(ds.current.plus(newdir));
                        } else {
                            ds.current = ds.current.plus(newdir);
                            ds.wipPattern.getAngles().remove(ds.wipPattern.getAngles().size() - 1);
                        }
                    } else {
                        boolean success = ds.wipPattern.tryAppendDir(newdir);
                        if(success) {
                            ds.current = idealNextLoc;
                        }
                    }
                }
            }
        }
        return false;
    }
    @Override
    public boolean mouseClicked(double x, double y, int i) {
        if(super.mouseClicked(x, y, i)) {
            return true;
        }
        if(HexConfig.client().clickingTogglesDrawing()) {
            if(drawState instanceof PatternDrawState.BetweenPatterns) {
                return drawStart(x, y);
            } else {
                return drawEnd();
            }
        }
        return drawStart(x, y);
    }
    @Override
    public void mouseMoved(double x, double y) {
        super.mouseMoved(x, y);
        if(HexConfig.client().clickingTogglesDrawing() && !(drawState instanceof PatternDrawState.BetweenPatterns)) {
            drawMove(x, y);
        }
    }
    @Override
    public boolean mouseDragged(double x, double y, int i, double dx, double dy) {
        if(super.mouseDragged(x, y, i, dx, dy)) {
            return true;
        }
        if(HexConfig.client().clickingTogglesDrawing()) {
            return false;
        }
        return drawMove(x, y);
    }
    @Override
    public boolean mouseReleased(double x, double y, int i) {
        if(super.mouseReleased(x, y, i)) {
            return true;
        }
        if(HexConfig.client().clickingTogglesDrawing()) {
            return false;
        }
        return drawEnd();
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float f) {
        super.render(graphics, x, y, f);
        PoseStack ps = graphics.pose();
        Matrix4f mat = ps.last().pose();
        ShaderInstance prevShader = RenderSystem.getShader();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        Vec2 mousePos = new Vec2(x, y);
        HexCoord mouseCoord = pxToCoord(mousePos);
        int radius = 3;
        for (Iterator<HexCoord> it = mouseCoord.rangeAround(radius); it.hasNext(); ) {
            HexCoord dotCoord = it.next();
            Vec2 dotPx = coordToPx(dotCoord);
            double delta = dotPx.add(mousePos.negated()).length();
            double scaledDist = Mth.clamp(
                    1F - ((delta - hexSize()) / (radius * hexSize())),
                    0F,
                    1F
            );
            RenderLib.drawSpot(
                    mat,
                    dotPx,
                    (float) (scaledDist * 2),
                    (float) Mth.lerp(scaledDist, 0.4F, 0.5F),
                    (float) Mth.lerp(scaledDist, 0.8f, 1.0f),
                    (float) Mth.lerp(scaledDist, 0.7f, 0.9f),
                    (float) scaledDist
            );
        }
        RenderSystem.defaultBlendFunc();
        if(!(drawState instanceof PatternDrawState.BetweenPatterns)) {
            List<Vec2> points = new ArrayList<>();
            Set<Integer> dupIndices = null;
            if(drawState instanceof PatternDrawState.JustStarted ds) {
                points.add(coordToPx(ds.start));
            } else if(drawState instanceof PatternDrawState.Drawing ds) {
                dupIndices = RenderLib.findDupIndices(ds.wipPattern.positions());
                for(var pos : ds.wipPattern.positions()) {
                    Vec2 pix = coordToPx(pos.plus(ds.start));
                    points.add(pix);
                }
            }
            points.add(mousePos);
            RenderLib.drawPatternFromPoints(
                    mat,
                    points,
                    dupIndices,
                    false,
                    0xff64c8ff,
                    0xfffecbe6,
                    0.1F,
                    RenderLib.DEFAULT_READABILITY_OFFSET,
                    1F,
                    0.0
            );
        }
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(() -> prevShader);
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    public float hexSize() {
        var scaleModifier = Minecraft.getInstance().player.getAttributeValue(HexAttributes.GRID_ZOOM);
        var baseScale = Math.sqrt(width * height / 512.0);
        return (float) (baseScale / scaleModifier);
    }
    public Vec2 coordsOffset() {
        return new Vec2((float) (width * 0.5), (float) (height * 0.5));
    }
    public Vec2 coordToPx(HexCoord coord) {
        return at.petrak.hexcasting.api.utils.HexUtils.coordToPx(coord, hexSize(), coordsOffset());
    }
    public HexCoord pxToCoord(Vec2 px) {
        return at.petrak.hexcasting.api.utils.HexUtils.pxToCoord(px, hexSize(), coordsOffset());
    }
    public static class PatternDrawState {
        public static class BetweenPatterns extends PatternDrawState {

        }
        public static class JustStarted extends PatternDrawState {
            public final HexCoord start;
            public JustStarted(HexCoord start) {
                this.start = start;
            }
        }
        public static class Drawing extends PatternDrawState {
            public final HexCoord start;
            public HexCoord current;
            public final HexPattern wipPattern;
            public Drawing(HexCoord start, HexCoord current, HexPattern wipPattern) {
                this.start = start;
                this.current = current;
                this.wipPattern = wipPattern;
            }
        }
    }
}
