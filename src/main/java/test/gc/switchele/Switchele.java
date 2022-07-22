package test.gc.switchele;

import emu.grasscutter.plugin.Plugin;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.player.PlayerJoinEvent;

import test.gc.switchele.commands.SwitchElement;
import test.gc.switchele.commands.*;

/**
 * The Grasscutter plugin template.
 * This is the main class for the plugin.
 */
public final class Switchele extends Plugin {
    /* Turn the plugin into a singleton. */
    private static Switchele instance;

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
        this.getLogger().info("The example plugin has been loaded.");
    }

    /**
     * This method is called before the servers are started, or when the plugin enables.
     */
    @Override public void onEnable() {
        // Register commands.
        this.getHandle().registerCommand(new SwitchElement());

        // Log a plugin status message.
        this.getLogger().info("The example plugin has been enabled.");
    }

    /**
     * This method is called when the plugin is disabled.
     */
    @Override public void onDisable() {
        // Log a plugin status message.
        this.getLogger().info("The example plugin has been disabled.");
    }
}
