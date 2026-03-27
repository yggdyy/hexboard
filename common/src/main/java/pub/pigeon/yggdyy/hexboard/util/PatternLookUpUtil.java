package pub.pigeon.yggdyy.hexboard.util;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.math.HexCoord;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.api.mod.HexTags;
import at.petrak.hexcasting.api.utils.HexUtils;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import pub.pigeon.yggdyy.hexboard.HexBoardConfig;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.BookPage;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.*;

public class PatternLookUpUtil {
    public static Optional<Map.Entry<ResourceKey<ActionRegistryEntry>, ActionRegistryEntry> > lookUpIdCommon(HexPattern pattern) {
        return HexActions.REGISTRY.entrySet().stream().filter(entry -> pattern.sigsEqual(entry.getValue().prototype())).findAny();
    }
    public static Optional<Map.Entry<ResourceKey<ActionRegistryEntry>, ActionRegistryEntry> > lookUpIdPerWorld(HexPattern pattern) {
        List<List<HexCoord>> targets = new ArrayList<>();
        targets.add(pattern.positions().stream().distinct().sorted(Comparator.comparingInt(HexCoord::getQ).thenComparingInt(HexCoord::getR)).toList());
        for(HexDir dir : HexDir.values()) {
            if(dir.equals(pattern.getStartDir())) continue;
            HexPattern _pattern = new HexPattern(dir, pattern.getAngles());
            targets.add(_pattern.positions().stream().distinct().sorted(Comparator.comparingInt(HexCoord::getQ).thenComparingInt(HexCoord::getR)).toList());
        }
        return HexActions.REGISTRY.entrySet().stream().filter(entry -> {
            if(!HexUtils.isOfTag(HexActions.REGISTRY, entry.getKey().location(), HexTags.Actions.PER_WORLD_PATTERN)) {
                return false;
            }
            if(entry.getValue().prototype().getAngles().size() == pattern.getAngles().size()) {
                List<HexCoord> now = entry.getValue().prototype().positions().stream().distinct().sorted(Comparator.comparingInt(HexCoord::getQ).thenComparingInt(HexCoord::getR)).toList();
                boolean flag = false;
                for(List<HexCoord> target : targets) {
                    if(target.size() == now.size()) {
                        boolean flag1 = true;
                        HexCoord o1 = target.get(1), o2 = now.get(1);
                        for(int i = 0; i < target.size(); ++i) {
                            HexCoord c1 = target.get(i), c2 = now.get(i);
                            if(c1.getQ() - o1.getQ() != c2.getQ() - o2.getQ() || c1.getR() - o1.getR() != c2.getR() - o2.getR()) {
                                flag1 = false;
                                break;
                            }
                        }
                        if(flag1) {
                            flag = true;
                            break;
                        }
                    }
                }
                return flag;
            }
            return false;
        }).findAny();
    }
    public static Pair<Optional<ResourceLocation>, Optional<Integer>> lookUpPatternPage(HexPattern pattern) {
        String patSig = pattern.anglesSignature();
        Book book = BookRegistry.INSTANCE.books.get(HexAPI.modLoc("thehexbook"));
        if(book != null) {
            for (ResourceLocation cid : HexBoardConfig.config.patchouliSearchCategories.stream().map(ResourceLocation::new).toList()) {
                BookCategory category = book.getContents().categories.get(cid);
                if (category != null) {
                    for (BookEntry e : category.getEntries()) {
                        List<BookPage> pages = e.getPages();
                        for (int i = 0; i < pages.size(); ++i) {
                            BookPage p = pages.get(i);
                            JsonObject root = p.sourceObject;
                            try {
                                List<JsonObject> patsJson = root.getAsJsonArray("patterns").asList().stream().map(JsonElement::getAsJsonObject).toList();
                                for (JsonObject jsonObject : patsJson) {
                                    if(jsonObject.get("signature").getAsString().equals(patSig)) {
                                        return Pair.of(Optional.of(e.getId()), Optional.of(i));
                                    }
                                }
                            } catch (Exception ignored) {

                            }
                        }
                    }
                }
            }
        }
        return Pair.of(Optional.empty(), Optional.empty());
    }
    public static Pair<Optional<ResourceLocation>, Optional<Integer>> lookUpIdPage(ResourceLocation id) {
        String ids = id.toString();
        Book book = BookRegistry.INSTANCE.books.get(HexAPI.modLoc("thehexbook"));
        if(book != null) {
            for (ResourceLocation cid : HexBoardConfig.config.patchouliSearchCategories.stream().map(ResourceLocation::new).toList()) {
                BookCategory category = book.getContents().categories.get(cid);
                if (category != null) {
                    for (BookEntry e : category.getEntries()) {
                        List<BookPage> pages = e.getPages();
                        for (int i = 0; i < pages.size(); ++i) {
                            BookPage p = pages.get(i);
                            JsonObject root = p.sourceObject;
                            try {
                                if(root.get("op_id").getAsString().equals(ids)) {
                                    return Pair.of(Optional.of(e.getId()), Optional.of(i));
                                }
                            } catch (Exception ignored) {

                            }
                        }
                    }
                }
            }
        }
        return Pair.of(Optional.empty(), Optional.empty());
    }
}
