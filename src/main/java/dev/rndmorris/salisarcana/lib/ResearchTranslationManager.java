package dev.rndmorris.salisarcana.lib;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import dev.rndmorris.salisarcana.SalisArcana;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.common.MinecraftForge;
import thaumcraft.api.research.ResearchCategories;

import java.util.HashMap;
import java.util.HashSet;

public final class ResearchTranslationManager {
    private static final HashMap<String, HashMap<String, String>> customLangKeys = new HashMap<>();
    private static final HashSet<String> overriddenNames = new HashSet<>();
    private static final HashSet<String> overriddenTexts = new HashSet<>();
    private ResearchTranslationManager() {}
    public static void generateStandardKeys() {
        StringTranslate translator = StringTranslate.getInstance();

        for(final var category : ResearchCategories.researchCategories.values()) {
            for(final var research : category.research.values()) {
                String nameKey = "tc.research_name." + research.key;
                String textKey = "tc.research_text." + research.key;

                if(!translator.containsTranslateKey(nameKey)) {
                    //noinspection unchecked
                    translator.languageList.put(nameKey, research.getName());
                    overriddenNames.add(research.key);
                }

                if(!translator.containsTranslateKey(textKey)) {
                    //noinspection unchecked
                    translator.languageList.put(textKey, research.getText());
                    overriddenTexts.add(research.key);
                }
            }
        }

        updateTranslator();
    }

    public static void injectCustomLangKeys() {
        //noinspection unchecked
        StringTranslate.getInstance().languageList.putAll(customLangKeys.get(SalisArcana.proxy.getLanguageCode()));
        updateTranslator();
    }

    public static void setResearchName(final String researchKey, final String name, final String language) {
        setLangKey("tc.research_name." + researchKey, name, language);

        if(overriddenNames.contains(researchKey)) {
            final var research = ResearchWrapper.wrap(ResearchCategories.getResearch(researchKey));
            research.overrideName = true;
        }
    }

    public static void setResearchText(final String researchKey, final String text, final String language) {
        setLangKey("tc.research_text." + researchKey, text, language);

        if(overriddenTexts.contains(researchKey)) {
            final var research = ResearchWrapper.wrap(ResearchCategories.getResearch(researchKey));
            research.overrideText = true;
        }
    }

    private static void setLangKey(final String key, final String value, final String language) {
        if(FMLLaunchHandler.side().isServer() && !language.equals("en_US")) return;

        customLangKeys.computeIfAbsent(language, k -> new HashMap<>()).put(key, value);

        if(SalisArcana.proxy.getLanguageCode().equals(language)) {
            //noinspection unchecked
            StringTranslate.getInstance().languageList.put(key, value);
            updateTranslator();
        }
    }

    private static void updateTranslator() {
        StringTranslate.getInstance().lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }
}
