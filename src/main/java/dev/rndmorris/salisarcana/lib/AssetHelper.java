package dev.rndmorris.salisarcana.lib;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

import cpw.mods.fml.common.Loader;

public class AssetHelper {

    // TODO: check obfuscation
    private static Map<String, String> LANGUAGE;
    static {
        try {
            // noinspection unchecked
            LANGUAGE = R.of(
                R.of(StatCollector.class)
                    .get("localizedName", StringTranslate.class))
                .get("languageList", Map.class);
        } catch (Exception e) {
            // noinspection unchecked
            LANGUAGE = R.of(
                R.of(StatCollector.class)
                    .get("field_74839_a", StringTranslate.class))
                .get("field_74816_c", Map.class);
        }
    }

    public static void addLangEntry(String key, String value) {
        LANGUAGE.put(key, value);
    }

    public static void copyResearchFiles() {
        final String CONFIG_PATH = "config/salisarcana/research";
        final String RESOURCE_PATH = "assets/salisarcana/research";

        File configDir = new File(CONFIG_PATH);
        if (!configDir.exists()) {
            if (!configDir.mkdirs()) {
                System.err.println("Failed to create config directory: " + CONFIG_PATH);
                return;
            }
        }
        copyFiles(RESOURCE_PATH, CONFIG_PATH);
    }

    private static boolean copyFiles(String resourcePath, String outputPath) {
        try {
            Path outputDir = Paths.get(outputPath);
            try (JarFile self = new JarFile(
                Loader.instance()
                    .activeModContainer()
                    .getSource())) {
                Enumeration<JarEntry> entries = self.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(resourcePath) && !entry.isDirectory()) {
                        String parsedEntryName = entryName.substring(entryName.lastIndexOf("/") + 1);
                        Path targetPath = outputDir.resolve(parsedEntryName);
                        if (!targetPath.toFile()
                            .exists()) {
                            Files.copy(self.getInputStream(entry), targetPath, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            LOG.error(e);
            return false;
        }
    }
}
