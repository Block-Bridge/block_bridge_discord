package me.quickscythe;

import json2.JSONArray;
import json2.JSONObject;
import me.quickscythe.bot.Bot;
import me.quickscythe.utils.Utils;
import me.quickscythe.webapp.WebApp;

import java.io.*;

public class Main {


    private static boolean DEBUG = false;
    private static JSONObject CONFIG;
    private static Bot BOT;
    private static WebApp WEB_APP;


    public static void main(String[] args) {
        Utils._before_init();

        CONFIG = loadConfig();
        checkConfigDefaults();
        saveConfig();
        BOT = new Bot();
        WEB_APP = new WebApp();

    }

    private static void checkConfigDefaults() {
        if (!CONFIG.has("command_prefix"))
            CONFIG.put("command_prefix", "!");
        if (!CONFIG.has("cmd_channel"))
            CONFIG.put("cmd_channel", 0L);
        if (!CONFIG.has("log_channel"))
            CONFIG.put("log_channel", 0L);
        if (!CONFIG.has("api_entry_point"))
            CONFIG.put("api_entry_point", "/api");
        if (!CONFIG.has("app_entry_point"))
            CONFIG.put("app_entry_point", "/app");
        if (!CONFIG.has("inactive_days_timer"))
            CONFIG.put("inactive_days_timer", 90);
        if (!CONFIG.has("web_port"))
            CONFIG.put("web_port", 8585);
        if (!CONFIG.has("token_valid_time"))
            CONFIG.put("token_valid_time", 24);
        if (!CONFIG.has("guild_id"))
            CONFIG.put("guild_id", 0L);
        if(!CONFIG.has("allow"))
            CONFIG.put("allow", new JSONArray());
        if (!CONFIG.has("bot_token")) {
            CONFIG.put("bot_token",0L);
            Utils.getLogger().error("Bot token not found in config file. Please enter your bot token in the config file.", "=");
            saveConfig();
            throw new RuntimeException("Bot token not found in config file.");
        }
    }

    private static JSONObject loadConfig() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File config = new File("config.json");
            if (!config.exists()) if (config.createNewFile()) {
                Utils.getLogger().error("Config file generated.", "=");
            }
            BufferedReader reader = new BufferedReader(new FileReader("config.json"));

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
            FileWriter f2 = new FileWriter("config.json", false);
            f2.write(CONFIG.toString(2));
            f2.close();
        } catch (IOException e) {
            Utils.getLogger().log("There was an error saving the config file.", true);
            Utils.getLogger().error("Error", e);
        }
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public static Bot getBot() {
        return BOT;
    }

    public static WebApp getWebApp() {
        return WEB_APP;
    }

    public static JSONObject getConfig() {
        return CONFIG;
    }

    public static int TOKEN_VALID_TIME() {
        return CONFIG.getInt("token_valid_time");
    }
}