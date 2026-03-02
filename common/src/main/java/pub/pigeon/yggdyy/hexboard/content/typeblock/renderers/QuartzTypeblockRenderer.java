package pub.pigeon.yggdyy.hexboard.content.typeblock.renderers;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.client.render.PatternColors;
import at.petrak.hexcasting.client.render.PatternRenderer;
import at.petrak.hexcasting.client.render.WorldlyPatternRenderHelpers;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import pub.pigeon.yggdyy.hexboard.HexBoard;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;
import pub.pigeon.yggdyy.hexboard.content.ModKeyMappings;
import pub.pigeon.yggdyy.hexboard.content.ModModels;
import pub.pigeon.yggdyy.hexboard.content.board.BoardBlockEntity;
import pub.pigeon.yggdyy.hexboard.content.interaction.BoardClient;
import pub.pigeon.yggdyy.hexboard.content.typeblock.TypeblockRenderers;
import pub.pigeon.yggdyy.hexboard.content.typeblock.items.QuartzTypeblockItem;
import pub.pigeon.yggdyy.hexboard.util.ItemStackUtil;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.client.book.ClientBookRegistry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.*;

public class QuartzTypeblockRenderer implements TypeblockRenderers.IRenderer {
    public static final ResourceLocation MODEL_ID = HexBoard.modLoc("typeblock/quartz");
    public static final List<ResourceLocation> FAVOUR_CATEGORY = new ArrayList<>(List.of(
            HexAPI.modLoc("patterns"),
            HexAPI.modLoc("patterns/spells"),
            HexAPI.modLoc("patterns/great_spells")
    ));
    public Cache cache = null;
    @Override
    public void renderOnBoard(ItemStack itemStack, BoardBlockEntity entity, float f, PoseStack stack, MultiBufferSource buffer, int light, int overlay, BlockEntityRendererProvider.Context context) {
        VertexConsumer consumer = buffer.getBuffer(ItemBlockRenderTypes.getRenderType(entity.getBlockState(), true));
        context.getBlockRenderDispatcher().getModelRenderer().renderModel(stack.last(), consumer, entity.getBlockState(), ModModels.getCustomModel(MODEL_ID), 1f, 1f, 1f, light, overlay);
        if(!itemStack.isEmpty() && itemStack.getItem() instanceof QuartzTypeblockItem item) {
            CompoundTag tag = item.readIotaTag(itemStack);
            if(tag != null && Objects.equals(IotaType.getTypeFromTag(tag), HexIotaTypes.PATTERN)) {
                var data = tag.get(HexIotaTypes.KEY_DATA);
                if(data != null) {
                    HexPattern pattern = PatternIota.deserialize(data).getPattern();
                    stack.translate(0, 0.25, 0.126);
                    stack.mulPose(new Quaternionf().rotateX((float) Math.PI));
                    stack.scale(0.25f, 0.25f, 0.25f);
                    PatternRenderer.renderPattern(pattern, stack, WorldlyPatternRenderHelpers.READABLE_SCROLL_SETTINGS, PatternColors.singleStroke(0xff_000000 | HexBoardConfig.config.quartzTypeblockPatternColor), 0, 128);
                }
            }
        }
    }
    @Override
    public void renderAsTarget(ItemStack itemStack, GuiGraphics graphics, float f) {
        QuartzTypeblockItem item = (QuartzTypeblockItem) itemStack.getItem();
        CompoundTag tag = item.readIotaTag(itemStack);
        if(tag != null) {
            graphics.drawString(Minecraft.getInstance().font, Component.translatable("item.hexboard.quartz_typeblock").append(": ").append(IotaType.getDisplay(tag)), BoardClient.getHUDX(), BoardClient.getHUDY() + HexBoardConfig.config.hudLineHeight * 2, 0xFF_000000 | HexBoardConfig.config.hudInfoQuartzTypeblockColor);
            HexPattern _pat;
            try {
                _pat = PatternIota.deserialize(Objects.requireNonNull(tag.get(HexIotaTypes.KEY_DATA))).getPattern();
            } catch (Exception e) {
                _pat = null;
            }
            HexPattern pat = _pat;
            if(pat != null) {
                //graphics.drawString(Minecraft.getInstance().font, Component.translatable("item.hexboard.quartz_typeblock").append(": ").append(pat.toString()), BoardClient.getHUDX(), BoardClient.getHUDY() + HexBoardConfig.config.hudLineHeight * 2, 0xFF_FFFFFF);
                graphics.pose().pushPose();
                graphics.pose().translate(BoardClient.getHUDX(), BoardClient.getHUDY() + HexBoardConfig.config.hudLineHeight * 3, 600);
                graphics.pose().scale(HexBoardConfig.config.hudLineHeight * 6, HexBoardConfig.config.hudLineHeight * 6, 1);
                PatternRenderer.renderPattern(pat, graphics.pose(), WorldlyPatternRenderHelpers.READABLE_SCROLL_SETTINGS, PatternColors.DEFAULT_GRADIENT_COLOR.withDots(true, true), 0, 512);
                graphics.pose().popPose();
                if(cache != null && cache.stack == itemStack) {
                    MutableComponent name = Component.empty();
                    if(cache.name != null) {
                        name.append(cache.name);
                    }
                    if(cache.entry != null) {
                        name.append(Component.translatable("info.hexboard.patchouli", ModKeyMappings.REFER_PATCHOULI.getTranslatedKeyMessage()));
                        if(ModKeyMappings.REFER_PATCHOULI.isDown()) {
                            ClientBookRegistry.INSTANCE.displayBookGui(HexAPI.modLoc("thehexbook"), cache.entry, cache.page);
                        }
                    }
                    graphics.drawString(Minecraft.getInstance().font, name, BoardClient.getHUDX(), BoardClient.getHUDY() + 9 * HexBoardConfig.config.hudLineHeight, 0xFF_000000 | HexBoardConfig.config.hudInfoQuartzTypeblockColor);
                } else {
                    Component name = null;
                    ResourceLocation entry = null;
                    int pageI = 0;
                    Optional<Map.Entry<ResourceKey<ActionRegistryEntry>, ActionRegistryEntry> > patEntryOpt = HexActions.REGISTRY.entrySet().stream().filter(e -> pat.anglesSignature().equals(e.getValue().prototype().anglesSignature())).findAny();
                    if(patEntryOpt.isPresent()) {
                        name = Component.translatable("hexcasting.action." + patEntryOpt.get().getKey().location());
                        Player player = Minecraft.getInstance().player;
                        if(player != null && ItemStackUtil.isPlayerHas(player, PatchouliAPI.get().getBookStack(HexAPI.modLoc("thehexbook")))) {
                            Book book = BookRegistry.INSTANCE.books.get(HexAPI.modLoc("thehexbook"));
                            if (book != null) {
                                boolean flag = false;
                                for (ResourceLocation cid : FAVOUR_CATEGORY) {
                                    BookCategory category = book.getContents().categories.get(cid);
                                    if (category != null) {
                                        for (BookEntry e : category.getEntries()) {
                                            List<BookPage> pages = e.getPages();
                                            for (int i = 0; i < pages.size(); ++i) {
                                                BookPage p = pages.get(i);
                                                JsonObject root = p.sourceObject;
                                                try {
                                                    if (root.get("op_id").getAsString().equals(patEntryOpt.get().getKey().location().toString())) {
                                                        flag = true;
                                                        entry = e.getId();
                                                        pageI = i;
                                                        break;
                                                    }
                                                } catch (Exception ignored) {

                                                }
                                            }
                                            if (flag) {
                                                break;
                                            }
                                        }
                                    }
                                    if (flag) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    cache = new Cache(itemStack, name, entry, pageI);
                }
            }
        }
    }
    public record Cache(ItemStack stack, @Nullable Component name, @Nullable ResourceLocation entry, int page) {

    }
}
