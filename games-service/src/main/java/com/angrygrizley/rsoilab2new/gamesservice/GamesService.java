package com.angrygrizley.rsoilab2new.gamesservice;

import java.util.List;

public interface GamesService {
    void addGame(Game game);
    List<Game> getGames();
    Game getGameById(Long id) throws GameNotFoundException;
    Game putGame(Game newGame) throws GameNotFoundException;
    List<Game> searchByGenre(String genre);
    List<Game> searchByPlayerNum(int num);
    void deleteGame(Long id);
}
