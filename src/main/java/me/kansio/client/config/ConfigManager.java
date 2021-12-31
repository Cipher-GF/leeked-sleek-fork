package me.kansio.client.config;

import com.google.gson.*;
import lombok.Getter;
import lombok.Setter;
import me.kansio.client.Client;
import me.kansio.client.modules.impl.Module;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConfigManager {

    @Getter
    @Setter
    private File dir;

    public ConfigManager(File dir) {
        this.dir = dir;
        loadConfigs();
    }

    @Getter
    private CopyOnWriteArrayList<Config> configs = new CopyOnWriteArrayList<>();

    public void loadConfigs() {
        configs.clear();
        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (dir != null) {
                File[] files = dir.listFiles(f -> !f.isDirectory() && FilenameUtils.getExtension(f.getName()).equals("sleek"));
                for (File f : files) {
                    Config config = new Config(FilenameUtils.removeExtension(f.getName()).replace(" ", ""), f);
                    configs.add(config);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void loadConfig(String configName) {
        try {
            Reader reader = new FileReader(new File(dir, configName + ".sleek"));
            JsonElement node = new JsonParser().parse(reader);

            if (!node.isJsonArray()) {
                return;
            }

            JsonArray arr = node.getAsJsonArray();
            arr.forEach(element -> {
                JsonObject obj = element.getAsJsonObject();
                String modName = obj.get("name").getAsString();
                Module m = Client.getInstance().getModuleManager().getModuleByName(modName);
                if (m != null) {
                    m.load(obj);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void saveConfig(String cfgName) {
        // yes config is purposely misspelled
        File config = new File(dir, cfgName + ".sleek");
        try {

            if (!config.exists()) {
                config.createNewFile();
            }
            Writer typeWriter = new FileWriter(config);
            JsonArray arr = new JsonArray();

            Client.getInstance().getModuleManager().getModules().forEach(module -> arr.add(module.save()));

            String finalJson = new GsonBuilder().setPrettyPrinting().create().toJson(arr);
            System.out.println(finalJson);
            typeWriter.write(finalJson);
            typeWriter.close();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            config.delete();
        }
    }

    public void removeConfig(String cfg) {
        File f = new File(dir, cfg + ".sleek");
        if (f.exists()) {
            try {
                Files.delete(f.toPath());
            } catch (IOException e) {
            }
        }
        configs.remove(cfg);
    }
}
