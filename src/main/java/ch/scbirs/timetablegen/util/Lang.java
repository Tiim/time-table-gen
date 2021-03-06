package ch.scbirs.timetablegen.util;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
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
                url = Lang.class.getResource("/lang/en.properties");
            }
            p.load(url.openStream());
        } catch (IOException e) {
        }
    }

    public static String[] translate(String[] keys) {
        return Arrays.stream(keys).map(Lang::translate).toArray(String[]::new);
    }
}
