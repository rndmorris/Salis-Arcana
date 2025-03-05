package dev.rndmorris.salisarcana.lib;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import dev.rndmorris.salisarcana.SalisArcana;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;

public final class TranslationManager {

    private static final HashMap<String, HashMap<String, String>> customLangKeys = new HashMap<>();

    private TranslationManager() {}

    /***
     * Injects the standard translation keys for researches that use them.
     * Is called once at startup and every time the language table is cleared (resource reloads.)
     */
    public static void generateStandardKeys() {
        StringTranslate translator = StringTranslate.getInstance();

        for (final var category : ResearchCategories.researchCategories.values()) {
            for (final var research : category.research.values()) {
                String nameKey = "tc.research_name." + research.key;
                String textKey = "tc.research_text." + research.key;

                if (!translator.containsTranslateKey(nameKey)) {
                    // noinspection unchecked
                    translator.languageList.put(nameKey, research.getName());
                }

                if (!translator.containsTranslateKey(textKey)) {
                    // noinspection unchecked
                    translator.languageList.put(textKey, research.getText());
                }
            }
        }

        updateTranslator();
    }

    /***
     * Replaces any custom lang keys that were reset by a resource reload.
     * Should only be called from the {@link net.minecraft.client.resources.IResourceManagerReloadListener} in
     * {@link dev.rndmorris.salisarcana.ClientProxy}
     */
    public static void injectCustomLangKeys() {
        final var langKeys = customLangKeys.get(SalisArcana.proxy.getLanguageCode());
        @SuppressWarnings("unchecked")
        final Map<String, String> langMap = StringTranslate.getInstance().languageList;

        if (langKeys != null) {
            langMap.putAll(langKeys);
            updateTranslator();
        }

        final var fallbackKeys = customLangKeys.get("en_US");
        if (fallbackKeys != null) {
            for (final var entry : fallbackKeys.entrySet()) {
                langMap.putIfAbsent(entry.getKey(), entry.getValue());
            }
        }
    }

    /***
     * Sets a language key and value pair for en_US.
     * Automatically updates the language table when appropriate.
     *
     * @param key   The translation key to create or update.
     * @param value The translated text to set the key to.
     */
    public static void setLangEntry(final String key, final String value) {
        setLangEntry(key, value, "en_US");
    }

    /***
     * Sets a language key and value pair for the specified language.
     * Automatically updates the language table when appropriate.
     *
     * @param key      The translation key to create or update.
     * @param value    The translated text to set the key to.
     * @param language The language code for the translation (use "en_US" by default.)
     */
    public static void setLangEntry(final String key, final String value, final String language) {
        final var isDefault = language.equals("en_US");
        if (FMLLaunchHandler.side()
            .isServer() && !isDefault) return;

        customLangKeys.computeIfAbsent(language, k -> new HashMap<>())
            .put(key, value);

        if (SalisArcana.proxy.getLanguageCode()
            .equals(language)
            || (isDefault && !StringTranslate.getInstance()
                .containsTranslateKey(key))) {
            // noinspection unchecked
            StringTranslate.getInstance().languageList.put(key, value);
            updateTranslator();
        }
    }

    /***
     * Searches the current language map for a value and returns the first key found, if it is present.
     * Originally written by Midnight.
     *
     * @param value The translated text to search for.
     * @return The translation key for that text, or null if it wasn't found.
     */
    public static @Nullable String lookupLangEntryByValue(String value) {
        @SuppressWarnings("unchecked")
        final Map<String, String> langEntries = StringTranslate.getInstance().languageList;

        final var keys = langEntries.entrySet()
            .stream()
            .filter(
                e -> e.getValue()
                    .equals(value))
            .map(Map.Entry::getKey)
            // In almost all cases, there should only be one key for a given value
            .limit(1)
            .toArray(String[]::new);
        return ArrayHelper.tryGet(keys, 0)
            .data();
    }

    /***
     * Translates research using the standard translation key.
     * Required for the research override injection.
     *
     * @param research
     * @return
     */
    public static String getResearchName(ResearchItem research) {
        return StatCollector.translateToLocal("tc.research_name." + research.key);
    }

    public static String getResearchText(ResearchItem research) {
        return StatCollector.translateToLocal("tc.research_text." + research.key);
    }

    /***
     * Resets the "last update time" so any ChatComponentTranslations will clear their cache.
     */
    private static void updateTranslator() {
        StringTranslate.getInstance().lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }
}
