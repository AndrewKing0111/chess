package model;

public record RequestCreateGame(String authToken, String gameName, String whiteUserName, String blackUserName) {}
