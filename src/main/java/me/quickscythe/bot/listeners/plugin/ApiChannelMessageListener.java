package me.quickscythe.bot.listeners.plugin;

import me.quickscythe.BlockBridgeDiscordPlugin;
import me.quickscythe.api.listener.Listener;
import me.quickscythe.utils.BlockBridgeDiscordUtils;

public class ApiChannelMessageListener implements Listener.ApiChannelListener {

    private final BlockBridgeDiscordPlugin plugin;

    public ApiChannelMessageListener(BlockBridgeDiscordPlugin plugin) {
        this.plugin = plugin;
        BlockBridgeDiscordUtils.getMain().getApi().getWebApp().addListener(plugin, this);
    }

    @Override
    public void onMessage(me.quickscythe.api.event.api.ApiChannelMessageEvent event) {
        if (event.getAction().equalsIgnoreCase("send_message") && event.getTo().equalsIgnoreCase("discord"))
            BlockBridgeDiscordUtils.getMain().getBot().getLogsChannel().sendMessage(event.getMessage()).queue();
    }
}
