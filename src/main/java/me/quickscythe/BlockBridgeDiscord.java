package me.quickscythe;

import me.quickscythe.api.BotPlugin;
import me.quickscythe.api.config.ConfigClass;
import me.quickscythe.bot.Bot;
import me.quickscythe.utils.BlockBridgeDiscordUtils;

public class BlockBridgeDiscord extends ConfigClass {


    private boolean DEBUG = false;
    private Bot BOT;
    private BlockBridgeApi bba;

    public BlockBridgeDiscord() {
        super(new BlockBridgeDiscordPlugin(), "bot");
        checkConfigDefaults();
        finish();

        BOT = new Bot(this);
        bba = new BlockBridgeApi();
        bba.init(true);
    }


    public static void main(String[] args) {
        BlockBridgeDiscordUtils._before_init();
        BlockBridgeDiscordUtils.initMain(new BlockBridgeDiscord());
        BlockBridgeDiscordUtils.initPluginLoader();
        BotPlugin plugin = BlockBridgeDiscordUtils.getMain().getConfig().getPlugin();
        BlockBridgeDiscordUtils.getPluginLoader().registerPlugin(plugin);
        BlockBridgeDiscordUtils.getPluginLoader().enablePlugin(plugin);
    }

    private void checkConfigDefaults() {

        setDefault("command_prefix", "!");
        setDefault("cmd_channel", 0L);
        setDefault("log_channel", 0L);
        setDefault("guild_id", 0L);
//        setDefault("logs", new JSONObject());
//        JSONObject logs = getConfig().getData().getJSONObject("logs");
//        setDefault("join", false, logs);
//        setDefault("leave", false, logs);
//        setDefault("status_changes", false, logs);
//        setDefault("status_changes_message", "Status: {0}", logs);
//        setDefault("chat", false, logs);
//        setDefault("join_message", "[0] has joined!", logs);
//        setDefault("leave_message", "[0] has left!", logs);
//        setDefault("chat_format", "<[0]> [1]", logs);
        if (!getConfig().getData().has("bot_token")) {
            setDefault("bot_token", 0L);

            finish();
            BlockBridgeDiscordUtils.getLogger().error("Bot token not found in config file. Please enter your bot token in the config file.", new RuntimeException("Bot token not found in config file."));

        }
    }

    public boolean isDebug() {
        return DEBUG;
    }

    public Bot getBot() {
        return BOT;
    }


    public BlockBridgeApi getApi() {
        return bba;
    }


}