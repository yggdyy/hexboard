package pub.pigeon.yggdyy.hexboard.content.interaction.staff;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;

import java.util.ArrayList;
import java.util.List;

public class StaffModes {
    public static double scroll = 0;
    public interface IMode {
        void click();
        void shiftClick();
        Component display();
        default void tickCritical() {

        }
        default void render(PoseStack stack, MultiBufferSource source, float tickDelta, Camera camera) {
            if(BoardClient.board != null && 0 <= BoardClient.target && BoardClient.target < BoardClient.board.slots.size()) {
                BoardClient.renderSingleOutline(stack, source, tickDelta, camera, BoardClient.target, BoardClient.target, HexBoardConfig.config.boardTargetColor | 0x40_000000, 0.128);
            }
        }
        @Nullable
        default KeyMapping getTriggerKey() {
            return null;
        }
    }
    public static final List<IMode> MODES = new ArrayList<>();
    public static void init() {
        MODES.clear();
        add(new AddMode());
        add(new DeleteMode());
        add(new MoveMode());
        add(new CopyMode());
        add(new ResolveMode(false));
        add(new ResolveMode(true));
        add(new LoadMode());
    }
    public static void add(IMode mode) {
        MODES.add(mode);
    }
    public static boolean handleScroll(double delta) {
        if(Minecraft.getInstance().screen != null) {
            return false;
        }
        Player player = Minecraft.getInstance().player;
        if(player != null && player.isShiftKeyDown()) {
            if(player.getMainHandItem().getItem() instanceof BoardStaffItem || player.getOffhandItem().getItem() instanceof BoardStaffItem) {
                scroll += delta;
                return true;
            }
        }
        return false;
    }
    public static void handleModeChange(Minecraft minecraft) {
        if(scroll != 0) {
            int now = MODES.indexOf(BoardClient.staffMode);
            int next = (now + (scroll > 0 ? 1 : -1) + MODES.size()) % MODES.size();
            BoardClient.staffMode = MODES.get(next);
            scroll = 0;
            if (minecraft.player != null) {
                minecraft.player.displayClientMessage(BoardClient.staffMode.display(), true);
            }
        } else {
            for(IMode mode : MODES) {
                if(mode.getTriggerKey() != null && mode.getTriggerKey().isDown()) {
                    BoardClient.staffMode = mode;
                    if (minecraft.player != null) {
                        minecraft.player.displayClientMessage(BoardClient.staffMode.display(), true);
                    }
                    break;
                }
            }
        }
    }
}
