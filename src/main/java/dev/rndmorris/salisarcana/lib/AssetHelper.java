package dev.rndmorris.salisarcana.lib;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

import cpw.mods.fml.common.Loader;

public class AssetHelper {

    private static Map<String, String> LANGUAGE;
    static {
        var localizedName = "field_74839_a";
        var languageList = "field_74816_c";
        if (Arrays.stream(StatCollector.class.getDeclaredFields())
            .anyMatch(
                f -> f.getName()
                    .equals("localizedName"))) {
            // use deobfuscated names
            localizedName = "localizedName";
            languageList = "languageList";
        }
        // noinspection unchecked
        LANGUAGE = R.of(
            R.of(StatCollector.class)
                .get(localizedName, StringTranslate.class))
            .get(languageList, Map.class);
    }

    public static String lookupLangEntryByValue(String value) {
        ArrayList<String> keys = new ArrayList<>();
        LANGUAGE.entrySet()
            .stream()
            .filter(
                e -> e.getValue()
                    .equals(value))
            .forEach(e -> keys.add(e.getKey()));
        if (keys.isEmpty()) {
            return null;
        }
        return keys.get(0); // In almost all cases, there should only be one key for a given value
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
        if (extractFilesFromResources(RESOURCE_PATH, CONFIG_PATH)) {
            LOG.info("Successfully copied research files to config directory.");
        } else {
            LOG.error("Failed to copy research files to config directory.");
        }
    }

    /**
     * Extracts files from the mod's resources to a destination directory.
     *
     * @param resource    The resource path in the jar to extract from.
     * @param destination The destination directory on the filesystem to extract to.
     * @return True if the extraction was successful, false otherwise.
     */
    private static boolean extractFilesFromResources(String resource, String destination) {
        try {
            Path outputDir = Paths.get(destination);
            try (JarFile self = new JarFile(
                Loader.instance()
                    .activeModContainer()
                    .getSource())) {
                Enumeration<JarEntry> entries = self.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(resource) && !entry.isDirectory()) {
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
