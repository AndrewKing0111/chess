package model;

import java.util.HashSet;

public record GameListData(HashSet<GameData> games) {
    public HashSet<GameData> getGames() { return games; }
}
