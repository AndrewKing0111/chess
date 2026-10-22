package server;

import com.google.gson.Gson;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
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

        javalin.post("/user", this::createUser);

    }

    public void clear(Context ctx) {
        try {
            service.clear();
            ctx.status(200);
            ctx.json("{}");
        } catch (DataAccessException error) {
            var serializer = new Gson();
            var objToSerialize = new ResultError(error);
            var json = serializer.toJson(objToSerialize);
            ctx.status(500);
            ctx.result(json);
        }
    }

    public void createUser(Context ctx) {
        var parser = new Gson();
        var request = parser.fromJson(ctx.body(), RequestCreateUser.class);
        try {
            users.createUser(request);
        } catch (DataAccessException error) {
            return;
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
