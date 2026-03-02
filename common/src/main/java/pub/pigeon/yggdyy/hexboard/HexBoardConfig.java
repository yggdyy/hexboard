package pub.pigeon.yggdyy.hexboard;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class HexBoardConfig{
    public static Data config = new Data();
    @Config(name = HexBoard.MOD_ID)
    @Config.Gui.Background("hexboard:textures/gui/config.png")
    public static class Data implements ConfigData{
        @ConfigEntry.Category(value = "board")
        @ConfigEntry.ColorPicker
        public int boardSectionColor = 0x0000FF;
        @ConfigEntry.Category(value = "board")
        @ConfigEntry.ColorPicker
        public int boardTargetColor = 0xFFF176;
        @ConfigEntry.Category(value = "board")
        @ConfigEntry.ColorPicker
        public int boardGoodColor = 0x00FF00;
        @ConfigEntry.Category(value = "board")
        @ConfigEntry.ColorPicker
        public int boardBadColor = 0xFF0000;
        @ConfigEntry.Category(value = "board")
        @ConfigEntry.ColorPicker
        public int quartzTypeblockPatternColor = 0xfecbe6;
        @ConfigEntry.Category(value = "hud")
        public int hudLineHeight = 10;
        @ConfigEntry.Category(value = "hud")
        public int hudInfoX = 2;
        @ConfigEntry.Category(value = "hud")
        public int hudInfoY = 2;
        @ConfigEntry.Category(value = "hud")
        @ConfigEntry.ColorPicker
        public int hudInfoStaffModeColor = 0xFFFFFF;
        @ConfigEntry.Category(value = "hud")
        @ConfigEntry.ColorPicker
        public int hudInfoTargetIndexColor = 0xFFFFFF;
        @ConfigEntry.Category(value = "hud")
        @ConfigEntry.ColorPicker
        public int hudInfoQuartzTypeblockColor = 0xFFFFFF;
        @ConfigEntry.Category(value = "hud")
        @ConfigEntry.ColorPicker
        public int hudInfoWoolTypeblockColor = 0xFFFFFF;
        @ConfigEntry.Category(value = "resolve")
        public int maxResolvedIota = (int) 1e5;
    }
}
