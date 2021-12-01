package fr.cesi.maker.utils;

import fr.cesi.maker.utils.json.JSONReader;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.json.JSONObject;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class FileLang {
    private final Logger log = JDALogger.getLog(FileLang.class);
    private final JSONObject fr_FR = getFile();

    public String get(String key) {
        if (key.isEmpty()) return "Error";

        String language;
        String text = key;

        text = getString(fr_FR, key);
        return text;
    }


    private JSONObject getFile() {
        try {
            String path = "config/lang.json";
            File file = new File(path);
            if (file.exists()) {
                return new JSONReader(file).toJSONObject();
            }
            log.warn("Le fichier  pas");
        } catch (IOException e) {
            log.warn("Il y a eux une erreur");
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private String getString(JSONObject object, String key) {
        if (!object.has(key)) {
            System.out.println("Erreur : " + key + " existe pas");
            return key;
        }
        return object.getString(key);
    }

}