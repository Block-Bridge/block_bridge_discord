package me.quickscythe.bot.listeners;

import me.quickscythe.BlockBridgeDiscord;
import me.quickscythe.api.BotPlugin;
import me.quickscythe.sql.SqlUtils;
import me.quickscythe.utils.BlockBridgeDiscordUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.sql.ResultSet;
import java.sql.SQLException;
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
            SqlUtils.getDatabase("core").update("DELETE FROM servers WHERE 1");
            SqlUtils.getDatabase("core").update("DROP TABLE servers");
        }
        if (cmd.equals(main.getBot().CMD_PREFIX() + "test")) {
            SqlUtils.getDatabase("core").update("INSERT INTO servers (name, ip) VALUES ('test', 'test')");

        }
        if (cmd.equals(main.getBot().CMD_PREFIX() + "servers")) {
            ResultSet rs = SqlUtils.getDatabase("core").query("SELECT * FROM servers");

            try {
                while (rs.next())
                    event.getChannel().sendMessage("Server: " + rs.getString("name") + " IP: " + rs.getString("ip")).queue();
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
