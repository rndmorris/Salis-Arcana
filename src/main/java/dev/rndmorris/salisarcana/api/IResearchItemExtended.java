package dev.rndmorris.salisarcana.api;

/**
 * Implement this on your {@link thaumcraft.api.research.ResearchItem} derivative to enable compatibility with Salis
 * Arcana.
 */
public interface IResearchItemExtended {

    /**
     * The translation key at which this research's human-readable name can be found.
     */
    String getNameTranslationKey();

    /**
     * The translation key at which this research's human-readable subtitle/lore text can be found.
     */
    String getTextTranslationKey();

}
