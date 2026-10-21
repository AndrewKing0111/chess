package dataaccess;

public interface UserDAO {
    public boolean getUserByUsername(String username);
    public boolean createUser(String username, String password, String email);
    public boolean deleteUser(String username);
}
