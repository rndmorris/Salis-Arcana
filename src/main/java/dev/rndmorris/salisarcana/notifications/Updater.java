package dev.rndmorris.salisarcana.notifications;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import dev.rndmorris.salisarcana.Tags;
import dev.rndmorris.salisarcana.config.SalisConfig;

public class Updater {

    public boolean hasCheckedVersion = !SalisConfig.enableVersionChecking;

    private static final String versionURL = "https://api.modrinth.com/v2/project/y1bqIjK6/version";

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!hasCheckedVersion) {
            // check for new version
            hasCheckedVersion = true;
            VersionInfo newVersion = checkForNewVersion();
            if (newVersion != null) {
                event.player.addChatMessage(
                    new ChatComponentTranslation("salisarcana:update_available", newVersion.getVersionNumber()));
                IChatComponent message = new ChatComponentTranslation("salisarcana:update_link");
                message.getChatStyle()
                    .setChatClickEvent(
                        new ClickEvent(
                            ClickEvent.Action.OPEN_URL,
                            "https://modrinth.com/mod/salis-arcana/version/" + newVersion.getVersionNumber()));
                event.player.addChatMessage(message);
            }
        }
    }

    private VersionInfo checkForNewVersion() {

        try {
            URL url = new URL(versionURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                LOG.error("Version Check Failed: HTTP GET Request Failed with Error Code : {}", conn.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            Gson gson = new Gson();
            List<VersionInfo> versions = gson.fromJson(reader, new TypeToken<List<VersionInfo>>() {}.getType());
            reader.close();
            conn.disconnect();

            versions.sort(
                Comparator.comparing(VersionInfo::getVersionNumber)
                    .reversed());
            VersionInfo latestVersion = versions.get(0);
            if (!latestVersion.getVersionNumber()
                .equals(Tags.VERSION)) {
                return latestVersion;
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }
}

@SuppressWarnings("unused")
class VersionInfo {

    private List<String> game_versions;
    private List<String> loaders;
    private String id;
    private String project_id;
    private String author_id;
    private boolean featured;
    private String name;
    private String version_number;
    private String changelog;
    private String changelog_url;
    private String date_published;
    private int downloads;
    private String version_type;
    private String status;
    private String requested_status;
    private List<File> files;
    private List<Dependency> dependencies;

    // Getters and setters
    public String getVersionNumber() {
        return version_number == null ? "0" : version_number;
    }
}

@SuppressWarnings("unused")
class VersionFile {

    private Map<String, String> hashes;
    private String url;
    private String filename;
    private boolean primary;
    private int size;
    private String file_type;

    // Getters and setters
}

@SuppressWarnings("unused")
class Dependency {

    private String version_id;
    private String project_id;
    private String file_name;
    private String dependency_type;

    // Getters and setters
}
