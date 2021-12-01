package fr.cesi.maker.utils;

import com.google.gson.JsonArray;
import fr.cesi.maker.utils.json.JSONReader;
import fr.cesi.maker.utils.json.JSONWriter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Configuration {

    private final JSONObject object;
    private final File file;

    public Configuration(String path) throws IOException {
        this.file = new File(path);
        if (file.exists()) {
            this.object = new JSONReader(file).toJSONObject();
        } else {
            object = new JSONObject();
        }
    }

    public void save() {
        try (JSONWriter writer = new JSONWriter(file)) {
            writer.write(this.object);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String defaultValue) {
        if (!object.has(key)) object.put(key, defaultValue);
        return object.getString(key);
    }

    public int getInt(String key, int defaultValue) {
        if (!object.has(key)) object.put(key, defaultValue);
        return object.getInt(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (!object.has(key)) object.put(key, defaultValue);
        return object.getBoolean(key);
    }

    public JSONArray getJSONArray (String key) {
        if (!object.has(key)) object.put(key, new JsonArray());
        return object.getJSONArray(key);
    }

    public JSONObject getJSONObject(String key) {
        if (!object.has(key)) object.put(key, new JSONObject());
        return object.getJSONObject(key);
    }
}
