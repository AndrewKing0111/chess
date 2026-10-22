package server;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import io.javalin.*;
import io.javalin.http.Context;
import services.Service;

public class Server {

    private final Javalin javalin;
    private UserDAO users = new MemoryUserDAO();
    private Service service = new Service(users);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.delete("/db", this::clear);

    }

    public void clear(Context ctx) throws DataAccessException {
        service.clear();
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
