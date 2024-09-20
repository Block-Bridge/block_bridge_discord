package me.quickscythe.bot.plugins;

import me.quickscythe.BlockBridgeDiscordPlugin;
import me.quickscythe.api.BotPlugin;
import me.quickscythe.utils.BlockBridgeDiscordUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PluginLoader {

    private Map<BotPlugin, ClassLoader> PLUGINS = new HashMap<>();

    public PluginLoader() {
        registerPlugins();
        enablePlugins();
    }

    public void registerPlugin(BotPlugin plugin) {
        PLUGINS.put(plugin, plugin.getClass().getClassLoader());
    }

    public void enablePlugin(BotPlugin plugin) {
        plugin.enable();
        BlockBridgeDiscordUtils.getLogger().log("Plugin [0] enabled.", plugin.getName());
    }

    private void registerPlugins() {
        File plugin_folder = new File("plugins");
        if (!plugin_folder.exists()) plugin_folder.mkdir();
        for (File file : plugin_folder.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                try {
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, BlockBridgeDiscordUtils.class.getClassLoader());
                    Properties properties = new Properties();
                    properties.load(classLoader.getResourceAsStream("plugin.properties"));
                    Class<? extends BotPlugin> loadedClass = (Class<? extends BotPlugin>) classLoader.loadClass(properties.getProperty("main"));
                    BotPlugin instance = loadedClass.getDeclaredConstructor().newInstance();
                    instance.setName(properties.getProperty("name"));
                    BlockBridgeDiscordUtils.getLogger().log("Loaded plugin [0].", instance.getName());
                    PLUGINS.put(instance, classLoader);

                } catch (Exception e) {
                    BlockBridgeDiscordUtils.getLogger().error("There was an error loading a plugin (" + file.getName() + ").", e);
                    e.printStackTrace();
                }
            }
        }
    }

    public Set<BotPlugin> getPlugins() {
        return PLUGINS.keySet();
    }

    public BotPlugin getPlugin(String name) {
        for (BotPlugin plugin : getPlugins()) {
            if (plugin.getName().equalsIgnoreCase(name)) return plugin;
        }
        return null;
    }

    public void disablePlugins() {
        BlockBridgeDiscordUtils.getLogger().log("Disabling plugins...");
        for (Map.Entry<BotPlugin, ClassLoader> entry : PLUGINS.entrySet()) {
            try {
                disablePlugin(entry.getKey());
            } catch (IOException e) {
                BlockBridgeDiscordUtils.getLogger().error("There was an error disabling a plugin (" + entry.getKey().getName() + ").", e);
                e.printStackTrace();
            }
        }
        PLUGINS.clear();
    }

    public void disablePlugin(BotPlugin plugin) throws IOException {
        plugin.disable();
        BlockBridgeDiscordUtils.getMain().getApi().getWebApp().removeListeners(plugin);
        String name = plugin.getName();
        if (PLUGINS.get(plugin) instanceof URLClassLoader urlClassLoader) urlClassLoader.close();
        BlockBridgeDiscordUtils.getLogger().log("Plugin [0] disabled.", name);
    }

    public void enablePlugins() {
        for (BotPlugin plugin : getPlugins()) {
            plugin.enable();
            BlockBridgeDiscordUtils.getLogger().log("Plugin [0] enabled.", plugin.getName());
        }
    }

    public void reloadPlugins() {

        disablePlugins();
        registerPlugins();
        enablePlugins();
    }
}
