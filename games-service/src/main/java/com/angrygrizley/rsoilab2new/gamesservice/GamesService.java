package com.angrygrizley.rsoilab2new.gamesservice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface GamesService {
    Game addGame(Game game);
    Page<Game> getGames(PageRequest p);
    Game getGameById(Long id) throws GameNotFoundException;
    Game putGame(Game newGame) throws GameNotFoundException;
    List<Game> searchGames(String genre, int num);
    void deleteGame(Long id);
}
