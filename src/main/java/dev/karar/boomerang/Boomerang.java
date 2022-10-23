package dev.karar.boomerang;

import dev.karar.boomerang.bone.Bone;
import dev.karar.boomerang.commands.GiveCommand;
import dev.karar.boomerang.listeners.InventoryListener;
import dev.karar.boomerang.listeners.PlayerListener;
import dev.karar.boomerang.utils.Utils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Boomerang extends JavaPlugin {

    private static Boomerang instance;

    private static final List<Bone> boomerangs = new ArrayList<>();

    private static FileConfiguration config;
    private static final HashMap<String, String> messageData = new HashMap<>();

    public static Boomerang getInstance() {
        return instance;
    }

    public List<Bone> getBoomerangs() {
        return boomerangs;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public HashMap<String, String> getMessageData() {
        return messageData;
    }

    @Override
    public void onEnable() {
        instance = this;

        registerCommands();
        registerEvents();
        createConfig();
        createMessages();
    }

    @Override
    public void onDisable() {
        for (Bone bone : getBoomerangs()) bone.endReturn();
    }

    private void registerCommands() {
        getCommand("boomerang").setExecutor(new GiveCommand());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }

    private void createConfig() {
        // Get (or create) config.yml file.
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void createMessages() {
        // Get (or create) messages.yml file.
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        // Cache messages.yml file values in hash.
        FileConfiguration config = YamlConfiguration.loadConfiguration(messagesFile);
        for (String message : config.getConfigurationSection("").getKeys(false)) {
            messageData.put(message, Utils.colorize(config.getString(message)));
        }
    }
}
