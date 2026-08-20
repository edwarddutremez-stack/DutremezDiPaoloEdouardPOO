package app.poo4examen.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonDataManager {

    private static final String FILE_PATH = "src/main/resources/app/poo4examen/data/taches.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static synchronized List<Tache> chargerTaches() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Tache>>() {}.getType();
            List<Tache> list = gson.fromJson(reader, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static synchronized void sauvegarderTaches(List<Tache> taches) {
        File file = new File(FILE_PATH);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(taches, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}