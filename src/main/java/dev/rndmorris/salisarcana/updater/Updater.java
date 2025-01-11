package dev.rndmorris.salisarcana.updater;

import static dev.rndmorris.salisarcana.SalisArcana.LOG;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import dev.rndmorris.salisarcana.Tags;
import dev.rndmorris.salisarcana.config.ConfigModuleRoot;

public class Updater {

    public boolean hasCheckedVersion = ConfigModuleRoot.enableVersionChecking; // if true, version checking is disabled,
    // so we don't check

    private static final String versionURL = "https://raw.githubusercontent.com/rndmorris/Salis-Arcana/refs/heads/versions/versions.json";

    public static Map<String, VersionDetails> versions;

    public Updater() {
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!hasCheckedVersion) {
            // check for new version
            hasCheckedVersion = true;
            String newVersion = checkForNewVersion();
            if (newVersion != null) {
                IChatComponent message = new ChatComponentText(
                    "A new version of Salis Arcana is available. Click here to automatically update.");
                message.getChatStyle()
                    .setChatClickEvent(
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "salis-arcana-update " + newVersion));
            }
        }
    }

    private String checkForNewVersion() {

        try {
            URL url = new URL(versionURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP GET Request Failed with Error Code : " + conn.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();
            conn.disconnect();
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, VersionDetails>>() {}.getType();
            versions = gson.fromJson(json.toString(), type);
            List<String> versionList = new ArrayList<>(versions.keySet());
            Collections.sort(versionList);
            String latestVersion = versionList.get(versionList.size() - 1);
            if (!latestVersion.equals(Tags.VERSION)) {
                return latestVersion;
            }
        } catch (Exception e) {
            LOG.error(e);
        }
        return null;
    }

    public static boolean downloadNewVersion(String version) {
        String baseUrl = "https://github.com/rndmorris/Salis-Arcana/releases/download/";
        VersionDetails details = versions.get(version);
        if (details != null) {
            String url = baseUrl + version + "/salisarcana-" + version + ".jar";
            try {
                URL downloadUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) downloadUrl.openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("HTTP GET Request Failed with Error Code : " + conn.getResponseCode());
                }
                ByteArrayInputStream stream = new ByteArrayInputStream(
                    conn.getInputStream()
                        .readAllBytes());
                conn.disconnect();

                // check hash
                String hash = generateMD5(stream);
                if (hash == null || !hash.equals(details.getRelease())) {
                    return false;
                }
                File file = new File("mods/salisarcana-" + version + ".jar");
                boolean success = file.createNewFile();
                if (!file.exists() || !success) {

                    return false;
                }
                FileUtils.writeByteArrayToFile(new File("mods/salisarcana-" + version + ".jar"), stream.readAllBytes());
                return true;
            } catch (Exception e) {
                LOG.error(e);
            }
        }
        return false;
    }

    public static String generateMD5(ByteArrayInputStream input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.readAllBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));

            // Pad with leading zeros if necessary
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }

            return hashtext.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String getUrl(String version) {
        return "https://github.com/rndmorris/Salis-Arcana/releases/download/" + version
            + "/salisarcana-"
            + version
            + ".jar";
    }

}

final class VersionDetails {

    private String dev;
    private String sources;
    private String release;

    public String getDev() {
        return dev;
    }

    public String getSources() {
        return sources;
    }

    public String getRelease() {
        return release;
    }
}
