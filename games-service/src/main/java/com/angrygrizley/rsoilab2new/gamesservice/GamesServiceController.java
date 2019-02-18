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
        logger.info("[POST] /games", game);
        gamesService.addGame(game);
    }

    @GetMapping(value = "/games")
    public List<Game> getGames() {
        logger.info("[GET] /games");
        return gamesService.getGames();
    }

    @GetMapping(value = "/games/id/{id}")
    public Game getGameById(@PathVariable Long id) throws GameNotFoundException {
        logger.info("[GET] /games/" + id);
        return gamesService.getGameById(id);
    }

    @PutMapping(value = "/games/edit")
    public Game putGame(@RequestBody Game game) throws GameNotFoundException {
        logger.info("[PUT] /games/edit");
        return gamesService.putGame(game);
    }

    @GetMapping(value = "/games/search")
    public List<Game> searchGames(@RequestParam (value = "genre", required = false, defaultValue = "") String genre,
                                    @RequestParam (value = "playernum", required = false, defaultValue = "-1") int num){
        logger.info("[GET] /games/search genre: " + genre + " playernum: " + num);
        return gamesService.searchGames(genre, num);
    }

    @DeleteMapping(value = "/game/delete/{id}")
    public void deleteGame(@PathVariable Long id)
    {
        logger.info("[DELETE] /game/delete/" + id);
        gamesService.deleteGame(id);
    }


}
