package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import model.LoginData;
import model.UserData;
import services.Service;

public class Server {

    private final Javalin javalin;
    private UserDAO users = new LocalStorageUserDAO();
    private GameDAO games = new LocalStorageGameDAO();
    private AuthDAO auths = new LocalStorageAuthDAO();
    private Service service = new Service(users, games, auths);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", this::clear);

        javalin.post("/user", this::registerUser);

        javalin.post("/session", this::loginUser);

        javalin.delete("/session", this::logout);

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
        var request = new Gson().fromJson(ctx.body(), UserData.class);
        try {
            var result = service.registerUser(request);
            ctx.status(200);
            var json = new Gson().toJson(result);
            ctx.result(json);
        } catch (DataAccessException error) {
            var objToSerialize = new ResultError(error);
            var json = new Gson().toJson(objToSerialize);
            ctx.result(json);

            switch (objToSerialize.getMessage()) {
                case "Error: bad request":
                    ctx.status(400);
                    return;
                case "Error: already taken":
                    ctx.status(403);
                    return;
                default:
                    ctx.status(500);
            }
        }
    }

    public void loginUser(Context ctx) {
        var request = new Gson().fromJson(ctx.body(), LoginData.class);
        try {
            var result = service.loginUser(request);
            ctx.status(200);
            var json = new Gson().toJson(result);
            ctx.result(json);
        } catch (DataAccessException error) {
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
                default:
                    ctx.status(500);
            }
        }
    }

    public void logout(Context ctx) {

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
