package model;

public record RequestJoinGame(String authToken, String playerColor, int gameID) {}
