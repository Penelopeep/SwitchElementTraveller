package test.gc.switchele;
import com.google.gson.Gson;
import emu.grasscutter.plugin.Plugin;
import test.gc.switchele.commands.SwitchElement;

import java.io.*;
import java.util.stream.Collectors;

/**
 * The Grasscutter plugin template.
 * This is the main class for the plugin.
 */
public final class Switchele extends Plugin {
    /* Turn the plugin into a singleton. */
    private static Switchele instance;
    private static File configFile;
    private static test.gc.switchele.PluginConfig configuration;

    public void reloadConfig(){
        try {
            File config = new File(this.getDataFolder(), "Settings.json");
            FileReader reader = new FileReader(config);
            configuration = new Gson().fromJson(reader, PluginConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the plugin instance.
     * @return A plugin singleton.
     */

    public static Switchele getInstance() {
        return instance;
    }
    
    /**
     * This method is called immediately after the plugin is first loaded into system memory.
     */
    @Override public void onLoad() {
        // Set the plugin instance.
        instance = this;

        // Log a plugin status message.
        this.getLogger().info("The SwitchElement has been loaded.");

        // Get the configuration file.
        configFile = new File(this.getDataFolder(), "Settings.json");

        // Load the configuration.
        try {
            if (!configFile.exists()) {
                try (FileWriter writer = new FileWriter(configFile)) {
                    InputStream configStream = this.getResource("config.json");
                    if (configStream == null) {
                        this.getLogger().error("Failed to save default config file.");
                    } else {
                        writer.write(new BufferedReader(
                                new InputStreamReader(configStream)).lines().collect(Collectors.joining("\n"))
                        );
                        writer.close();

                        this.getLogger().info("Saved default config file.");
                    }
                }
            }


        } catch (IOException exception) {
            this.getLogger().error("Failed to create config file.", exception);
        }
    }

    /**
     * This method is called before the servers are started, or when the plugin enables.
     */
    @Override public void onEnable() {
        // Register commands.
        this.getHandle().registerCommand(new SwitchElement());

        // Log a plugin status message.
        this.getLogger().info("The SwitchElement has been enabled.");
    }

    /**
     * This method is called when the plugin is disabled.
     */
    @Override public void onDisable() {
        // Log a plugin status message.
        this.getLogger().info("The SwitchElement has been disabled.");
    }
    public static PluginConfig getPluginConfig() {
        return configuration;
    }
}
