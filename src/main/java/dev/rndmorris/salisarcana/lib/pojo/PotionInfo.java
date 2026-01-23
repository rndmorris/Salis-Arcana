package dev.rndmorris.salisarcana.lib.pojo;

import static dev.rndmorris.salisarcana.config.SalisConfig.thaum;

import com.github.bsideup.jabel.Desugar;

import dev.rndmorris.salisarcana.config.settings.IntSetting;

/**
 * Used by the MixinConfig_PotionIds class to bundle information it needs to pass around within itself
 */
@Desugar
public record PotionInfo(String langKey, String loggingName, IntSetting setting) {

    public static PotionInfo taintPoison() {
        return new PotionInfo("potion.fluxtaint", "Taint Poison", thaum.taintPoisonId);
    }

    public static PotionInfo fluxFlu() {
        return new PotionInfo("potion.visexhaust", "Flux Flu", thaum.fluxFluId);
    }

    public static PotionInfo fluxPhage() {
        return new PotionInfo("potion.infvisexhaust", "Flux Phage", thaum.fluxPhageId);
    }

    public static PotionInfo unnaturalHunger() {
        return new PotionInfo("potion.unhunger", "Unnatural Hunger", thaum.unnaturalHungerId);
    }

    public static PotionInfo warpWard() {
        return new PotionInfo("potion.warpward", "Warp Ward", thaum.warpWardId);
    }

    public static PotionInfo deadlyGaze() {
        return new PotionInfo("potion.deathgaze", "Deadly Gaze", thaum.deadlyGazeId);
    }

    public static PotionInfo blurredVision() {
        return new PotionInfo("potion.blurred", "Blurred Vision", thaum.blurredVisionId);
    }

    public static PotionInfo sunScorned() {
        return new PotionInfo("potion.sunscorned", "Sun Scorned", thaum.sunScornedId);
    }

    public static PotionInfo thaumarhia() {
        return new PotionInfo("potion.thaumarhia", "Thaumarhia", thaum.thaumarhiaId);
    }

    public boolean enabled() {
        return setting.isEnabled();
    }

    public int id() {
        return setting.getValue();
    }

}
