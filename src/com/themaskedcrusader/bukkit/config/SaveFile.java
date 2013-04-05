package com.themaskedcrusader.bukkit.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class SaveFile {
    private ConfigAccessor saveFile;

    public  SaveFile(JavaPlugin plugin, String fileName) {
        saveFile = new ConfigAccessor(plugin, fileName);
//        saveFile.reloadConfig();
//        saveFile.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return saveFile.getConfig();
    }

    public ConfigAccessor get() {
        return saveFile;
    }

    public void saveConfig() {
        saveFile.saveConfig();
    }

    public void delete() {
        saveFile.delete();
    }
}
