package me.quickscythe;

import json2.JSONObject;
import me.quickscythe.bot.Bot;
import me.quickscythe.utils.Utils;
import me.quickscythe.webapp.listeners.DiscordLogListener;

import java.io.*;

public class BlockBridgeDiscord {


    private static boolean DEBUG = false;
    private static JSONObject CONFIG;
    private static Bot BOT;
    private static BlockBridgeApi bba;


    public static void main(String[] args) {
        Utils._before_init();

        CONFIG = loadConfig();
        checkConfigDefaults();
        saveConfig();
        BOT = new Bot();
        bba = new BlockBridgeApi();
        bba.init();
        bba.getWebApp().addListener(new DiscordLogListener());

    }

    private static void checkConfigDefaults() {

        setDefault("command_prefix", "!");
        setDefault("cmd_channel", 0L);
        setDefault("log_channel", 0L);
        setDefault("guild_id", 0L);
        setDefault("logs", new JSONObject());
        JSONObject logs = CONFIG.getJSONObject("logs");
        setDefault("join", false, logs);
        setDefault("leave", false, logs);
        setDefault("status_changes", false, logs);
        setDefault("status_changes_message", "Status: {0}", logs);
        setDefault("chat", false, logs);
        setDefault("join_message", "[0] has joined!", logs);
        setDefault("leave_message", "[0] has left!", logs);
        setDefault("chat_format", "<[0]> [1]", logs);
        if (!CONFIG.has("bot_token")) {
            setDefault("bot_token", 0L);

            saveConfig();
            Utils.getLogger().error("Bot token not found in config file. Please enter your bot token in the config file.", new RuntimeException("Bot token not found in config file."));

        }
    }

    private static void setDefault(String key, Object value) {
        setDefault(key, value, CONFIG);
    }

    private static void setDefault(String key, Object value, JSONObject config) {
        if(!config.has(key))
            config.put(key, value);
    }

    private static JSONObject loadConfig() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File config = new File("bot.json");
            if (!config.exists()) if (config.createNewFile()) {
                Utils.getLogger().log("Config file generated.");
            }
            BufferedReader reader = new BufferedReader(new FileReader("bot.json"));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();


        } catch (IOException ex) {
            Utils.getLogger().error("Config File couldn't be generated or accessed. Please check console for more details.", ex);
        }
        String config = stringBuilder.toString();
        return config.isEmpty() ? new JSONObject() : new JSONObject(config);
    }


    public static void saveConfig() {
        try {
            FileWriter f2 = new FileWriter("bot.json", false);
            f2.write(CONFIG.toString(2));
            f2.close();
        } catch (IOException e) {
            Utils.getLogger().error("There was an error saving the config file.", e);
        }
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static Bot getBot() {
        return BOT;
    }

//    public static WebApp getWebApp() {
//        return WEB_APP;
//    }

    public static JSONObject getConfig() {
        return CONFIG;
    }

    public static boolean LOG_STATUS() {
        return CONFIG.getJSONObject("logs").getBoolean("status_changes");
    }
    public static boolean LOG_JOIN() {
        return CONFIG.getJSONObject("logs").getBoolean("join");
    }
    public static boolean LOG_LEAVE() {
        return CONFIG.getJSONObject("logs").getBoolean("leave");
    }
    public static boolean LOG_CHAT() {
        return CONFIG.getJSONObject("logs").getBoolean("chat");
    }

    public static String LOG_STATUS_MSG() {
        return CONFIG.getJSONObject("logs").getString("status_changes_message");
    }

    public static String LOG_JOIN_MSG() {
        return CONFIG.getJSONObject("logs").getString("join_message");
    }

    public static String LOG_LEAVE_MSG() {
        return CONFIG.getJSONObject("logs").getString("leave_message");
    }

    public static String LOG_CHAT_MSG() {
        return CONFIG.getJSONObject("logs").getString("chat_format");
    }

    public static BlockBridgeApi getApi() {
        return bba;
    }
}