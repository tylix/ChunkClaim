package me.tylix.simplesurvival.config;

import com.google.gson.JsonObject;
import me.tylix.simplesurvival.SimpleSurvival;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonConfig {

    private final File file;

    private JsonObject defaults = new JsonObject();
    private JsonObject object;

    public JsonConfig(final File file) {
        this.file = file;
        reloadFromFile();
    }

    public boolean saveConfig() {
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (final IOException e) {
                return false;
            }
        }
        try (final FileWriter writer = new FileWriter(this.file)) {
            writer.write(SimpleSurvival.INSTANCE.getPrettyGson().toJson(this.object));
        } finally {
            return true;
        }
    }

    public boolean reloadFromFile() {
        if (!this.file.exists()) {
            if (this.object == null) this.object = this.defaults;
            return false;
        }
        try (final FileReader reader = new FileReader(this.file)) {
            this.object = SimpleSurvival.INSTANCE.getParser().parse(reader).getAsJsonObject();
        } catch (final IOException e) {
            return false;
        } finally {
            return true;
        }
    }

    public <T> T get(final String key, final Class<T> type) {
        if (!this.object.has(key)) return null;
        return SimpleSurvival.INSTANCE.getGson().fromJson(this.object.get(key), type);
    }

    public void set(final String key, final Object object) {
        this.object.add(key, SimpleSurvival.INSTANCE.getGson().toJsonTree(object));
    }

    public String toString() {
        return this.object.toString();
    }

    public void fromString(final String string) {
        this.object = SimpleSurvival.INSTANCE.getParser().parse(string).getAsJsonObject();
    }

}
