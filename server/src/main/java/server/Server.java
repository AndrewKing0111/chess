package server;

import com.google.gson.Gson;
import dataAccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import model.*;
import service.Service;

public class Server {

    private final Javalin javalin;
    private Service service = new Service();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", this::clear);

        javalin.post("/user", this::registerUser);

        javalin.post("/session", this::loginUser);

        javalin.delete("/session", this::logoutUser);

        javalin.post("/game", this::createGame);

        javalin.get("/game", this::listGames);

        javalin.put("/game", this::joinGame);
    }

    public void clear(Context ctx) {
        try {
            service.clear();
            ctx.status(200);
            ctx.json("{}");
        } catch (DataAccessException error) {
            var objToSerialize = new ResultError(error);
            var json = new Gson().toJson(objToSerialize);
            ctx.status(500);
            ctx.result(json);
        }
    }

    public void registerUser(Context ctx) {
        var registerRequest = new Gson().fromJson(ctx.body(), UserData.class);
        try {
            var result = service.registerUser(registerRequest);
            ctx.status(200);
            var json = new Gson().toJson(result);
            ctx.result(json);
        } catch (DataAccessException error) {
            setCTXState(ctx, error);
        }
    }

    public void loginUser(Context ctx) {
        var loginRequest = new Gson().fromJson(ctx.body(), LoginData.class);
        try {
            var result = service.loginUser(loginRequest);
            ctx.status(200);
            var json = new Gson().toJson(result);
            ctx.result(json);
        } catch (DataAccessException error) {
            setCTXState(ctx, error);
        }
    }

    public void logoutUser(Context ctx) {
        String logoutRequest = ctx.header("authorization");
        try {
            service.logoutUser(logoutRequest);
            ctx.status(200);
            ctx.result("{}");
        } catch (DataAccessException error) {
            setCTXState(ctx, error);
        }
    }

    public void createGame(Context ctx) {
        String authToken = ctx.header("authorization");
        var name = new Gson().fromJson(ctx.body(), GameNameData.class);
        var gameRequest = new RequestCreateGame(authToken, name.gameName(), null, null);
        try {
            int gameID = service.createGame(gameRequest);
            ctx.status(200);
            ctx.result(new Gson().toJson(new GameIDData(gameID)));
        } catch (DataAccessException error) {
            setCTXState(ctx, error);
        }
    }

    public void listGames(Context ctx) {
        String authToken = ctx.header("authorization");
        try {
            var games = service.listGames(authToken);
            ctx.status(200);
            ctx.result(new Gson().toJson(games));
        } catch (DataAccessException error) {
            setCTXState(ctx, error);
        }
    }

    public void joinGame(Context ctx) {
        String authToken = ctx.header("authorization");
        JoinGameData gameData = new Gson().fromJson(ctx.body(), JoinGameData.class);
        RequestJoinGame joinRequest = new RequestJoinGame(authToken, gameData.playerColor(), gameData.gameID());
        try {
            service.joinGame(joinRequest);
            ctx.status(200);
            ctx.result("{}");
        } catch (DataAccessException error) {
            setCTXState(ctx, error);
        }
    }

    public void setCTXState(Context ctx, DataAccessException error) {
        var objToSerialize = new ResultError(error);
        var json = new Gson().toJson(objToSerialize);
        ctx.result(json);

        switch (objToSerialize.getMessage()) {
            case "Error: bad request":
                ctx.status(400);
                return;
            case "Error: unauthorized":
                ctx.status(401);
                return;
            case "Error: already taken":
                ctx.status(403);
                return;
            default:
                ctx.status(500);
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
