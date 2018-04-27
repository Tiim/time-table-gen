package ch.scbirs.timetablegen;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

public class Lang {

    static Properties p;

    public static String translate(String key) {
        if (p == null) {
            loadLang();
        }
        return p.getProperty(key, key);
    }

    private static void loadLang() {
        String lang = Locale.getDefault().getLanguage();
        System.out.println("Language: " + lang);
        p = new Properties();
        try {
            URL url = Lang.class.getResource("/lang/" +lang + ".properties");
            if (url == null){
                url = Lang.class.getResource("/lang/de.properties");
            }
            p.load(url.openStream());
        } catch (IOException e) {
        }
    }

}
