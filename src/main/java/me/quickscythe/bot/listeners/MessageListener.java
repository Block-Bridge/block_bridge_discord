package me.quickscythe.bot.listeners;

import json2.JSONArray;
import me.quickscythe.Main;
import me.quickscythe.utils.Utils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;
        if (!event.getChannel().equals(Main.getBot().getCommandsChannel()) && !event.getChannel().equals(Main.getBot().getLogsChannel()))
            return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        String cmd = content.toLowerCase().split(" ")[0];
        String[] args = content.split(" ");

        if (cmd.equals(Main.getBot().CMD_PREFIX() + "update")) {
            Utils.getLogger().log("Updating the bot now.", true);
            Utils.update();

        }
        if (cmd.equals(Main.getBot().CMD_PREFIX() + "allow")) {
            if (args.length < 2) {
                event.getChannel().sendMessage("Usage: `" + Main.getBot().CMD_PREFIX() + "allow <ip>`").queue();
                return;
            }

            Main.getConfig().getJSONArray("allow").put(args[1]);
            Utils.getLogger().log("Added " + args[1] + " to the allow list.", true);
            Main.saveConfig();
        }
    }
}
