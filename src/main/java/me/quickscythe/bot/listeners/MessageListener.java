package me.quickscythe.bot.listeners;

import me.quickscythe.BlockBridgeDiscord;
import me.quickscythe.utils.BlockBridgeDiscordUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class MessageListener extends ListenerAdapter {

    BlockBridgeDiscord main;

    public MessageListener(BlockBridgeDiscord main) {
        this.main = main;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) return;
        if (!event.getChannel().equals(main.getBot().getCommandsChannel()) && !event.getChannel().equals(main.getBot().getLogsChannel()))
            return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        String cmd = content.toLowerCase().split(" ")[0];
        String[] args = content.split(" ");

        if (cmd.equals(main.getBot().CMD_PREFIX() + "update")) {
            BlockBridgeDiscordUtils.getLogger().log("Updating the bot now.");
            BlockBridgeDiscordUtils.update();

        }
        if (cmd.equals(main.getBot().CMD_PREFIX() + "allow")) {
            if (args.length < 2) {
                event.getChannel().sendMessage("Usage: `" + main.getBot().CMD_PREFIX() + "allow <ip>`").queue();
                return;
            }

            main.getApi().allow(args[1]);
//            Main.getConfig().getJSONArray("allow").put(args[1]);
            BlockBridgeDiscordUtils.getLogger().log("Added " + args[1] + " to the allow list.");
//            Main.saveConfig();
        }
    }
}
