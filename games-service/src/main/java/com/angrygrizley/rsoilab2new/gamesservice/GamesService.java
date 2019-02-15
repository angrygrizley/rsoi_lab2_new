package com.angrygrizley.rsoilab2new.gamesservice;

import java.util.List;

public interface GamesService {
    void addGame(Game game);
    List<Game> getGames();
    Game getGameById(Long id) throws com.angrygrizley.RSOI2.gamesservice.GameNotFoundException;
    Game putGame(Game newGame) throws com.angrygrizley.RSOI2.gamesservice.GameNotFoundException;
    List<Game> searchByGenre(String genre);
    List<Game> searchByPlayerNum(int num);
}
