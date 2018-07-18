package ch.scbirs.timetablegen;

import ch.scbirs.timetablegen.util.JSONProperty;
import com.google.common.io.PatternFilenameFilter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Persistence {

    private static final String CONFIG = "config.json";

    private final JSONProperty config;
    private final Gson json;
    private Path mainPath;

    public Persistence() {
        json = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        config = new JSONProperty(Paths.get(CONFIG));
        mainPath = Paths.get(config.get("last-path", "./tables"));
        if (Files.isRegularFile(mainPath)) {
            mainPath = mainPath.getParent();
        }
    }

    public Path save(Model model, Path path) {
        saveToFile(model, path);
        return Paths.get(".").toAbsolutePath().relativize(path.toAbsolutePath());
    }

    private void saveToFile(Model model, Path path) {
        mainPath.toFile().mkdirs();
        String j = json.toJson(model);
        try {
            Files.write(path, j.getBytes("UTF-8"));
        } catch (IOException ignored) {
        }
    }

    public File[] getFiles() throws IOException {
        if (!Files.isDirectory(mainPath)) {
            Files.createDirectories(mainPath);
        }
        File file = mainPath.normalize().toFile();
        File[] files = file.listFiles(new PatternFilenameFilter(".*\\.json"));
        return files == null ? new File[0] : files;
    }

    public Model load(File file) throws IOException {
        try (FileReader json = new FileReader(file)) {
            return this.json.fromJson(json, Model.class);
        }
    }

    public void setFolder(Path folder) {
        if (Files.isRegularFile(folder)) {
            folder = folder.getParent();
        }
        this.mainPath = folder;
        config.set("last-path", folder.toString());
    }

    public Path getFolder() {
        return mainPath;
    }
}
