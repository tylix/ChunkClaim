package me.tylix.simplesurvival.update;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import com.google.common.net.HttpHeaders;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.BiConsumer;

public class UpdateChecker {

    private static final String SPIGOT_URL = "https://api.spigotmc.org/legacy/update.php?resource=%d";

    private final JavaPlugin javaPlugin;

    private String currentVersion;
    private int resourceId = -1;
    private BiConsumer<VersionResponse, String> versionResponse;

    private UpdateChecker(@Nonnull JavaPlugin javaPlugin) {
        this.javaPlugin = Objects.requireNonNull(javaPlugin, "javaPlugin");
        this.currentVersion = javaPlugin.getDescription().getVersion();
    }

    public static UpdateChecker of(@Nonnull JavaPlugin javaPlugin) {
        return new UpdateChecker(javaPlugin);
    }

    public UpdateChecker currentVersion(@Nonnull String currentVersion) {
        this.currentVersion = currentVersion;
        return this;
    }

    public UpdateChecker resourceId(int resourceId) {
        this.resourceId = resourceId;
        return this;
    }

    public UpdateChecker handleResponse(@Nonnull BiConsumer<VersionResponse, String> versionResponse) {
        this.versionResponse = versionResponse;
        return this;
    }

    public void check() {
        Objects.requireNonNull(this.javaPlugin, "javaPlugin");
        Objects.requireNonNull(this.currentVersion, "currentVersion");
        Preconditions.checkState(this.resourceId != -1, "resource id not set");
        Objects.requireNonNull(this.versionResponse, "versionResponse");

        Bukkit.getScheduler().runTaskAsynchronously(this.javaPlugin, () -> {
            try {
                HttpURLConnection httpURLConnection = (HttpsURLConnection) new URL(String.format(SPIGOT_URL, this.resourceId)).openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty(HttpHeaders.USER_AGENT, "Mozilla/5.0");

                String fetchedVersion = Resources.toString(httpURLConnection.getURL(), Charset.defaultCharset());

                boolean latestVersion = fetchedVersion.equalsIgnoreCase(this.currentVersion);

                Bukkit.getScheduler().runTask(this.javaPlugin, () -> this.versionResponse.accept(latestVersion ? VersionResponse.LATEST : VersionResponse.FOUND_NEW, latestVersion ? this.currentVersion : fetchedVersion));
            } catch (IOException exception) {
                exception.printStackTrace();
                Bukkit.getScheduler().runTask(this.javaPlugin, () -> this.versionResponse.accept(VersionResponse.UNAVAILABLE, null));
            }
        });
    }
}