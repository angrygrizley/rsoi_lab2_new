package com.angrygrizley.rsoilab2new.gamesservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GamesServiceController {
    private GamesService gamesService;
    private Logger logger;

    @Autowired
    GamesServiceController(GamesService gamesService) {
        this.gamesService = gamesService;
        logger = LoggerFactory.getLogger(GamesServiceController.class);
    }

    @PostMapping(value = "/games")
    public void addGame(@RequestBody Game game) {
        gamesService.addGame(game);
        logger.info("[POST] /games", game);
    }

    @GetMapping(value = "/games")
    public List<Game> getGames() {
        logger.info("[GET] /games");
        return gamesService.getGames();
    }

    @GetMapping(value = "/games/id/{id}")
    public Game getGameById(@PathVariable Long id) throws com.angrygrizley.RSOI2.gamesservice.GameNotFoundException {
        logger.info("[GET] /games/" + id);
        return gamesService.getGameById(id);
    }

    @PutMapping(value = "/games/edit")
    public Game putGame(Game game) throws com.angrygrizley.RSOI2.gamesservice.GameNotFoundException {
        logger.info("[PUT] /games/edit");
        return gamesService.putGame(game);
    }

    @GetMapping(value = "/games/genre/{genre}")
    public List<Game> searchByGenre(@PathVariable String genre){
        logger.info("[GET] /games/genre/" + genre);
        return gamesService.searchByGenre(genre);
    }

    @GetMapping(value = "/games/playernum/{num}")
    public List<Game> searchByPlayerNum(@PathVariable int num) {
        logger.info("[GET] /games/playernum/" + num);
        return gamesService.searchByPlayerNum(num);
    }


}
