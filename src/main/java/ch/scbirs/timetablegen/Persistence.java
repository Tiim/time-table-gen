package ch.scbirs.timetablegen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Persistence {

    private final Gson json;
    private Path mainPath;

    public Persistence() {
        mainPath = Paths.get(".", "tables").toAbsolutePath();
        json = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public Path save(Model model) {
        String filename = model.getFileName();
        Path path = mainPath.resolve(filename + ".json");
        saveToFile(model, path);
        return path.normalize();
    }

    private void saveToFile(Model model, Path path) {
        mainPath.toFile().mkdirs();
        String j = json.toJson(model);
        try {
            Files.write(path, j.getBytes("UTF-8"));
        } catch (IOException ignored) {
        }
    }

    private void onSave(ActionEvent l) {


    }

    public File[] getFiles() {
        mainPath.toFile().mkdirs();
        return mainPath.normalize().toFile().listFiles();
    }

    public Model load(File file) throws FileNotFoundException {
        return json.fromJson(new FileReader(file), Model.class);
    }

    public void setFolder(Path folder) {
        this.mainPath = folder;
    }
}
