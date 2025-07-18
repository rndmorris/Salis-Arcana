package dev.rndmorris.salisarcana.mixins;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.ITargetMod;
import com.gtnewhorizon.gtnhmixins.builders.TargetModBuilder;

public enum TargetedMod implements ITargetMod {

    AUTOMAGY("Automagy"),
    THAUMCRAFT("Thaumcraft"),
    THAUMIC_TINKERER("ThaumicTinkerer"),
    BAUBLES_EXPANDED("Baubles|Expanded");

    private final TargetModBuilder builder;

    TargetedMod(String modId) {
        this.builder = new TargetModBuilder().setModId(modId);
    }

    @Nonnull
    @Override
    public TargetModBuilder getBuilder() {
        return builder;
    }
}
