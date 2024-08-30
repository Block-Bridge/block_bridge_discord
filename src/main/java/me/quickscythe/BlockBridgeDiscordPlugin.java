package me.quickscythe;
import me.quickscythe.api.BotPlugin;

public class BlockBridgeDiscordPlugin extends BotPlugin {

    public BlockBridgeDiscordPlugin() {
        setName("BlockBridgeDiscord");
    }

    public void enable() {
        System.out.println(getName() + " enabled");
    }
}
