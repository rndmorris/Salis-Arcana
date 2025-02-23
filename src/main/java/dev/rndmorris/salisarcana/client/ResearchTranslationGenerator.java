package dev.rndmorris.salisarcana.client;

import java.io.ByteArrayInputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.StringTranslate;

import org.apache.commons.io.Charsets;

import dev.rndmorris.salisarcana.lib.TranslationHelper;
import thaumcraft.api.research.ResearchCategories;

public class ResearchTranslationGenerator implements IResourceManagerReloadListener {

    public static void injectResearchProxyKeys() {
        StringBuilder dynamicKeys = new StringBuilder();

        for (final var category : ResearchCategories.researchCategories.values()) {
            for (final var research : category.research.values()) {
                String researchKey = TranslationHelper.getResearchTranslationKey(research);

                if (researchKey.startsWith("salisarcana.research_proxy.")) {
                    dynamicKeys.append(researchKey);
                    dynamicKeys.append('=');
                    dynamicKeys.append(research.getName());
                    dynamicKeys.append('\n');
                }
            }
        }

        StringTranslate.inject(
            new ByteArrayInputStream(
                dynamicKeys.toString()
                    .getBytes(Charsets.UTF_8)));
    }

    public static void initialize() {
        ((IReloadableResourceManager) Minecraft.getMinecraft()
            .getResourceManager()).registerReloadListener(new ResearchTranslationGenerator());
        ResearchTranslationGenerator.injectResearchProxyKeys();
    }

    @Override
    public void onResourceManagerReload(IResourceManager p_110549_1_) {
        ResearchTranslationGenerator.injectResearchProxyKeys();
    }
}
