package me.quickscythe.utils.runnables;

import me.quickscythe.BlockBridgeDiscord;
import me.quickscythe.utils.Utils;

import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Heartbeat extends TimerTask {

    private static long config_check = 0L;
    private static long token_check = 0L;


    public Heartbeat() {
    }

    @Override
    public void run() {

        Utils.getLogger().attemptQueue();
        long now = new Date().getTime();

        if (now - config_check >= Utils.convertTime(5, TimeUnit.MINUTES)) {
            config_check = now;
            BlockBridgeDiscord.saveConfig();
            Utils.getLogger().log("Checking for expired tokens...");
            BlockBridgeDiscord.getApi().getWebApp().runTokenCheck();
        }

    }
}
