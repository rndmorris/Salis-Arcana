package dev.rndmorris.salisarcana.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.ITargetMod;
import com.gtnewhorizon.gtnhmixins.builders.TargetModBuilder;

import cpw.mods.fml.common.Loader;

public enum TargetedMod implements ITargetMod {

    AUTOMAGY("Automagy"),
    GTNH_THAUMCRAFT_WANDS("gtnhtcwands"),
    THAUMCRAFT("Thaumcraft"),
    THAUMIC_TINKERER("ThaumicTinkerer"),
    TC4_TWEAKS("tc4tweak"),
    BAUBLES_EXPANDED("Baubles|Expanded"),
    UNDERGROUND_BIOMES("UndergroundBiomes"),
    NOT_ENOUGH_ITEMS("NotEnoughItems", "codechicken.nei.asm.NEICorePlugin"),
    HODGEPODGE("hodgepodge"),
    CONTROLLING("controlling"),
    MODERN_KEYBINDING("mkb"),
    ANGELICA("angelica"),
    ASPECT_RECIPE_INDEX("aspectrecipeindex");

    private final TargetModBuilder builder;
    public final String modId;
    private Boolean loadState = null;

    TargetedMod(String modId) {
        this.modId = modId;
        this.builder = new TargetModBuilder().setModId(modId);
    }

    TargetedMod(String modId, String coreModClass) {
        this(modId);
        this.builder.setCoreModClass(coreModClass);
    }

    public boolean isLoaded() {
        if (loadState == null) {
            loadState = Loader.isModLoaded(modId);
        }

        return loadState;
    }

    @Nonnull
    @Override
    public TargetModBuilder getBuilder() {
        return builder;
    }
}
