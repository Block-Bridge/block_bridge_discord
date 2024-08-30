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
    

    public Bot(){
        TOKEN = BlockBridgeDiscord.getConfig().getString("bot_token");
        GUILD_ID = BlockBridgeDiscord.getConfig().getLong("guild_id");
        LOG_CHANNEL = BlockBridgeDiscord.getConfig().getLong("log_channel");
        CMD_CHANNEL = BlockBridgeDiscord.getConfig().getLong("cmd_channel");
        API = JDABuilder.createDefault(TOKEN, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        BlockBridgeDiscordUtils.init(API);
        API.addEventListener(new MessageListener());
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
        return BlockBridgeDiscord.getConfig().getString("command_prefix");
    }
}
