package dataaccess;

import dataaccess.DatabaseDAO.DatabaseManager;

public class TestCustom {
    public static void main (String[] args) {
        try {
            DatabaseManager.createDatabase();
            var db = new DatabaseManager();
            db.configureDatabase();
        } catch (Exception e) {
            System.out.println("Failed!");
        }
    }
}
