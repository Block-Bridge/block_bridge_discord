package me.quickscythe.webapp.listeners;

import me.quickscythe.utils.BlockBridgeDiscordUtils;
import me.quickscythe.utils.listeners.Listener;

public class DiscordLogListener implements Listener.JoinListener, Listener.LeaveListener, Listener.StatusListener{
    @Override
    public void onJoin(String s, String s1, String s2) {
        BlockBridgeDiscordUtils.getLogger().log("Join: " + s);
    }

    @Override
    public void onLeave(String s, String s1, String s2) {
        BlockBridgeDiscordUtils.getLogger().log("Leave: " + s);
    }

    @Override
    public void onStatusChange(String s, String s1, String s2) {
        BlockBridgeDiscordUtils.getLogger().log("Status: " + s);
    }
}
