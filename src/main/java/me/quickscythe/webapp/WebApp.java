package me.quickscythe.webapp;

import json2.JSONArray;
import json2.JSONObject;
import me.quickscythe.Main;
import me.quickscythe.utils.Feedback;
import me.quickscythe.utils.Utils;
import me.quickscythe.utils.token.Token;
import me.quickscythe.utils.token.TokenManager;
import me.quickscythe.webapp.api.Api;
import me.quickscythe.webapp.api.FluxApi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.port;

public class WebApp {

    private final Api fluxApi = new FluxApi();

    public WebApp() {
        port(WEB_PORT());
        get(API_ENTRY_POINT(), (req, res) -> {
            res.type("application/json");
            return Feedback.Errors.json("No UUID provided");
        });
        get(API_ENTRY_POINT() + "/:uuid", (req, res) -> {
            String param = req.params(":uuid");
            Utils.getLogger().log("Got a connection");
            Utils.getLogger().log("Param: " + param);
            res.type("application/json");
            try {
                try {
                    return getFluxApi().getPlayerData(UUID.fromString(param)).toString();
                } catch (IllegalArgumentException ex) {
                    Utils.getLogger().log("Couldn't find UUID based on " + param);
                    if (getFluxApi().searchUUID(param) == null) {
                        Utils.getLogger().log("User " + param + " not found");
                        return Feedback.Errors.json("User not found");
                    }
                    Utils.getLogger().log("User " + param + " found.");
                    return getFluxApi().getPlayerData(getFluxApi().searchUUID(req.params(":uuid"))).toString();
                }
            } catch (SQLException ex) {
                return Feedback.Errors.json("Internal Server Error: Couldn't connect to SQL Database");
            }
        });


        get(APP_ENTRY_POINT(), (req, res) -> {
            res.type("application/json");
            return Feedback.Errors.json("No path provided");
        });

        get(APP_ENTRY_POINT() + "/v1/:token/:action", (req, res) -> {
            res.type("application/json");
            if (TokenManager.validToken(TokenManager.getToken(req.params(":token")), req))
                return Feedback.Errors.json("Invalid token");
            return Feedback.Success.json("Valid token. Action: " + req.params(":action"));


        });

        get(APP_ENTRY_POINT() + "/token", (req, res) ->

        {
            res.type("application/json");
            String token = TokenManager.requestNewToken(req.ip());
            return token == null ? Feedback.Errors.json("Error generating token. IP Not allowed?") : Feedback.Success.json(token);
        });

        get(APP_ENTRY_POINT() + "/tokens", (req, res) -> {
            res.type("application/json");
            JSONObject feedback = new JSONObject();
            feedback.put("tokens", new JSONArray());
            for (String token : TokenManager.getTokens(req.ip())) {
                feedback.getJSONArray("tokens").put(token);
            }
            return feedback;
        });

        Utils.getLogger().log("WebApp started on port " + WEB_PORT(), false);
    }

    public Api getFluxApi() {
        return fluxApi;
    }

    public void runTokenCheck() {
        List<String> remove_tokens = new ArrayList<>();
        for (Token token : TokenManager.getTokens()) {
            if (token.isExpired()) {
                Utils.getLogger().log("Token " + token.getToken() + " has expired.");
                remove_tokens.add(token.getToken());
            }
        }
        for (String token : remove_tokens) {
            TokenManager.removeToken(token);
        }
    }

    private String APP_ENTRY_POINT() {
        return Main.getConfig().getString("app_entry_point");
    }

    private String API_ENTRY_POINT() {
        return Main.getConfig().getString("api_entry_point");
    }

    private int WEB_PORT() {
        return Main.getConfig().getInt("web_port");
    }
}

