package dev.rndmorris.salisarcana.lib;

import java.util.WeakHashMap;

import net.minecraft.util.StatCollector;

import thaumcraft.api.research.ResearchItem;

public final class TranslationHelper {

    private static final WeakHashMap<ResearchItem, String> researchKeyCache = new WeakHashMap<>();

    private TranslationHelper() {}

    public static String getResearchTranslationKey(ResearchItem research) {
        // Step 1: Check the cache
        String key = researchKeyCache.get(research);
        if (key != null) return key;

        // Step 2: Check whether it follows the standard pattern.
        key = "tc.research_name." + research.key;
        if (research.getName() == StatCollector.translateToLocal(key)) {
            // Note: I'm intentionally avoiding .equals() here, since both strings will come from the same HashMap entry
            researchKeyCache.put(research, key);
            return key;
        }

        // Step 3: Fallback on dynamically generated keys
        key = "salisarcana.research_proxy." + research.key;
        researchKeyCache.put(research, key);
        return key;
    }
}
