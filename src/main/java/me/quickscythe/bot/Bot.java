package me.quickscythe.bot;

import me.quickscythe.BlockBridgeDiscord;
import me.quickscythe.bot.listeners.MessageListener;
import me.quickscythe.utils.BlockBridgeDiscordUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Bot {

    private final String TOKEN;
    private final JDA API;
    public final long GUILD_ID;
    public final long LOG_CHANNEL;
    public final long CMD_CHANNEL;
    private final BlockBridgeDiscord main;
    

    public Bot(BlockBridgeDiscord main) {
        this.main = main;
        TOKEN = main.getConfig().getData().getString("bot_token");
        GUILD_ID = main.getConfig().getData().getLong("guild_id");
        LOG_CHANNEL = main.getConfig().getData().getLong("log_channel");
        CMD_CHANNEL = main.getConfig().getData().getLong("cmd_channel");
        API = JDABuilder.createDefault(TOKEN, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        BlockBridgeDiscordUtils.initBot(API);
        API.addEventListener(new MessageListener(main));
    }

    public JDA getApi() {
        return API;
    }




    public Guild getGuild() {
        return API.getGuildById(GUILD_ID);
    }

    public TextChannel getLogsChannel() {
        return getGuild().getChannelById(TextChannel.class, LOG_CHANNEL);
    }

    public TextChannel getCommandsChannel() {
        return getGuild().getChannelById(TextChannel.class, CMD_CHANNEL);
    }


    public String CMD_PREFIX() {
        return main.getConfig().getData().getString("command_prefix");
    }
}
