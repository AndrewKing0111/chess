package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class Service {

    private UserDAO users;
    private GameDAO games;
    private AuthDAO auths;

    public Service(UserDAO users, GameDAO games, AuthDAO auths) {
        this.users = users;
        this.auths = auths;
        this.games = games;
    }

    public void clear() throws DataAccessException {
        users.clear();
        games.clear();
        auths.clear();
    }
}
