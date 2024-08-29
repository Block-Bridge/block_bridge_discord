package me.quickscythe.utils;

import me.quickscythe.utils.logs.BotLogger;
import me.quickscythe.utils.runnables.Heartbeat;
import me.quickscythe.webapp.api.Api;
import me.quickscythe.webapp.api.FluxApi;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static JDA api;
    private static BotLogger LOG;
    private static Api fluxApi;


    public static void _before_init() {
        LOG = new BotLogger("FluxApi");
    }

    public static void init(JDA api) {
        Utils.api = api;

//        SqlUtils.createDatabase("core", new SqlDatabase(SqlUtils.SQLDriver.MYSQL, "sql.vanillaflux.com", "vanillaflux", 3306, "sys", "9gGKGqthQJ&!#DGd"));
//        core = SqlUtils.getDatabase("core");
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Heartbeat(), convertTime(10, TimeUnit.SECONDS), convertTime(10, TimeUnit.SECONDS));

//        timer.schedule(new DailyCheck(timer), convertTime(10, TimeUnit.SECONDS));
    }

    public static String getContext(URL url) {
        //TODO better logging
        StringBuilder builder = new StringBuilder();
        try {
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                builder.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }



    public static BotLogger getLogger() {
        return LOG;
    }

    public static long convertTime(int duration, TimeUnit timeUnit) {
        return TimeUnit.MILLISECONDS.convert(duration, timeUnit);
    }



    public static void update() {
        try {
            saveStream(downloadFile("https://ci.vanillaflux.com/view/FluxVerse/job/biflux_bot/lastSuccessfulBuild/artifact/build/libs/fluxbot-1.0-all.jar", "admin", "&aaXffYj4#Pq@T3Q"), new FileOutputStream("fluxbot-1.0-all.jar"));
            getLogger().log("Update complete.", true);
            System.exit(1);
        } catch (Exception ex) {
            getLogger().error("There was an error updating the bot.", ex);
        }
    }

    public static InputStream downloadFile(String url, String... auth) {


        try {

            URL myUrl = new URI(url).toURL();
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setDoOutput(true);
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestMethod("GET");

            if (auth != null && auth.length >= 2) {
                String userCredentials = auth[0].trim() + ":" + auth[1].trim();
                String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));
                conn.setRequestProperty("Authorization", basicAuth);
            }
//            InputStream in = ;
//            FileOutputStream out = new FileOutputStream(filename);


            return conn.getInputStream();

        } catch (Exception ex) {
            getLogger().error("There was an error downloading that file.", ex);
        }

        return InputStream.nullInputStream();
    }

    public static void saveStream(InputStream in, FileOutputStream out) {
        try {
            int c;
            byte[] b = new byte[1024];
            while ((c = in.read(b)) != -1) out.write(b, 0, c);

            in.close();
            out.close();
        } catch (IOException ex) {
            getLogger().error("There was an error saving a downloaded file.", ex);
        }
    }


}
