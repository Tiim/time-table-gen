package ch.scbirs.timetablegen.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;

public class JSONProperty {

    private final Properties prop;
    private final Gson json;
    private final Path path;

    public JSONProperty(Path path) {
        this.path = path;
        json = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        prop = getProp();
    }

    private Properties getProp() {
        try {
            return json.fromJson(new FileReader(path.toFile()), Properties.class);
        } catch (FileNotFoundException e) {
            return new Properties();
        }
    }

    public String get(String key, String def) {
        return prop.getProperty(key, def);
    }

    public void set(String key, String val) {
        prop.setProperty(key, val);
        String s = json.toJson(prop);
        try {
            Files.write(path, Arrays.asList(s.split("\n")));
        } catch (IOException e) {
            System.out.println("Can't save property");
        }
    }

}
