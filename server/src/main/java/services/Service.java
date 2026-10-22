package services;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;

public class Service {

    private final UserDAO users;

    public Service(UserDAO users){
        this.users = users;
    }
    public void clear() throws DataAccessException {
        users.clear();
    }
}
