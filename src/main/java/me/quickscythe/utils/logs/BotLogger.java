package me.quickscythe.utils.logs;

import me.quickscythe.BlockBridgeDiscord;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BotLogger {
    private final Logger LOG;
    Map<Long, QueuedLog> queue = new HashMap<>();

    public BotLogger(String name) {
        this.LOG = LoggerFactory.getLogger(name);
    }

    public Logger getLog() {
        return LOG;
    }

    public void log(String message, String... args) {

        try {
            message = format(message, args);
            getLog().info(message);
            BlockBridgeDiscord.getBot().getLogsChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.GREEN).setTitle("INFO").setDescription(message).build()).queue();
        } catch (Exception ex) {
            queue.put(new Date().getTime(), new QueuedLog(LogLevel.INFO, message));
        }
    }


    public void warn(String message, String... args) {
        try {
            message = format(message, args);
            getLog().warn(message);
            BlockBridgeDiscord.getBot().getLogsChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.ORANGE).setTitle("WARN").setDescription(message).build()).queue();
        } catch (Exception ex) {
            queue.put(new Date().getTime(), new QueuedLog(LogLevel.WARN, message));
        }
    }

    public void error(String message, Exception e) {
        try {
            message = message.isEmpty() ? (e == null ? "Check the console" : e.getMessage()) : message;
            getLog().error(message);
            BlockBridgeDiscord.getBot().getLogsChannel().sendMessageEmbeds(new EmbedBuilder().setColor(Color.RED).setTitle("ERROR").setDescription(message).build()).queue();
            if(e!=null)for (StackTraceElement el : e.getStackTrace())
                getLog().error(el.toString());
        } catch (Exception ex) {
            queue.put(new Date().getTime(), new QueuedLog(LogLevel.ERROR, message));
        }
    }


    public void attemptQueue() {
        if (!queue.isEmpty()) {
            log("Attempting to dump queued logs");
            for (Map.Entry<Long, QueuedLog> entry : queue.entrySet()) {
                long time = entry.getKey();
                String message = entry.getValue().message;
                LogLevel level = entry.getValue().level;

                message = "[QUEUED] " + message;

                switch (level) {
                    case INFO -> log(message);
                    case WARN -> warn(message);
                    case ERROR -> error(message, null);
                }
            }
            queue.clear();
        }
    }

    public String format(String message, String... args) {
        for (int i = 0; i != args.length; i++) {
            message = message.replaceAll("\\[" + i + "]", args[i]);
        }
        return message;
    }

    private enum LogLevel {
        INFO, WARN, ERROR;
    }

    private class QueuedLog {
        String message;
        LogLevel level;

        private QueuedLog(LogLevel level, String message) {
            this.level = level;
            this.message = message;
        }

    }

}
