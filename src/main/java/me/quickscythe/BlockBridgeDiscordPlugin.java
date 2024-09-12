package me.quickscythe;
import me.quickscythe.api.BotPlugin;
import me.quickscythe.bot.listeners.plugin.ApiChannelMessageListener;

public class BlockBridgeDiscordPlugin extends BotPlugin {

    public BlockBridgeDiscordPlugin() {
        setName("BlockBridgeDiscord");
    }

    public void enable() {
        System.out.println(getName() + " enabled");

        new ApiChannelMessageListener(this);
    }
}
