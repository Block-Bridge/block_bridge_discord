package me.quickscythe.bot.listeners;

import json2.JSONObject;
import me.quickscythe.BlockBridgeDiscord;
import me.quickscythe.api.BotPlugin;
import me.quickscythe.api.object.Player;
import me.quickscythe.storage.Storage;
import me.quickscythe.storage.StorageManager;
import me.quickscythe.utils.BlockBridgeDiscordUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Objects;


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
        if (cmd.equals(main.getBot().CMD_PREFIX() + "reset")) {
            StorageManager.getStorage().set("players", new JSONObject());
            StorageManager.getStorage().set("servers", new JSONObject());
        }
        if (cmd.equals(main.getBot().CMD_PREFIX() + "test")) {
//            SqlUtils.getDatabase("core").update("INSERT INTO servers (name, ip) VALUES ('test', 'test')");
            BlockBridgeDiscordUtils.getMain().getApi().postData("send_message", new JSONObject().put("message", "test").put("action","send_message").put("to","minecraft"));

        }
        if (cmd.equals(main.getBot().CMD_PREFIX() + "servers")) {
            Storage storage = StorageManager.getStorage();
            if(storage.get("servers") == null) event.getChannel().sendMessage("No servers found.").queue();
            else {
                for (String key : ((JSONObject) storage.get("servers")).keySet()) {
                    JSONObject server = ((JSONObject) storage.get("servers")).getJSONObject(key);
                    event.getChannel().sendMessage("Server: " + server.getString("name") + " IP: " + server.getString("ip")).queue();
                }
            }


        }

        if (cmd.equals(main.getBot().CMD_PREFIX() + "players")) {

            Storage storage = StorageManager.getStorage();
            if(storage.get("players") == null) event.getChannel().sendMessage("No players found.").queue();
            else {
                for (String key : ((JSONObject) storage.get("players")).keySet()) {
                    Player player = new Player(((JSONObject) storage.get("players")).getJSONObject(key));
                    event.getChannel().sendMessage("Player: " + player.getName() + " Uid: " + player.getUid()).queue();
                }
            }


        }
        if (cmd.equals(main.getBot().CMD_PREFIX() + "plugins")) {
            String s = "";
            for (BotPlugin plugin : BlockBridgeDiscordUtils.getPluginLoader().getPlugins()) {
                s = Objects.equals(s, "") ? plugin.getName() : s + ", " + plugin.getName();
            }
            BlockBridgeDiscordUtils.getLogger().log("Plugins: " + s);

        }
        if (cmd.equals(main.getBot().CMD_PREFIX() + "reload")) {
            BlockBridgeDiscordUtils.getLogger().log("Reloading the bot now.");
            BlockBridgeDiscordUtils.getPluginLoader().reloadPlugins();
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
