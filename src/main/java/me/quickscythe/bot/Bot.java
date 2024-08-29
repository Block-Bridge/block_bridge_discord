package me.quickscythe.bot;

import me.quickscythe.Main;
import me.quickscythe.bot.listeners.MessageListener;
import me.quickscythe.utils.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Bot {

    private final String TOKEN;
    private final JDA API;
    public final long GUILD_ID;
    public final long LOG_CHANNEL;
    public final long CMD_CHANNEL;
    

    public Bot(){
        TOKEN = Main.getConfig().getString("bot_token");
        GUILD_ID = Main.getConfig().getLong("guild_id");
        LOG_CHANNEL = Main.getConfig().getLong("log_channel");
        CMD_CHANNEL = Main.getConfig().getLong("cmd_channel");
        API = JDABuilder.createDefault(TOKEN, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        Utils.init(API);
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
        return Main.getConfig().getString("command_prefix");
    }
}
