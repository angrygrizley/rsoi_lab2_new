package com.angrygrizley.rsoilab2new.gamesservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Game addGame(@RequestBody Game game) {
        logger.info("[POST] /games", game);
        return gamesService.addGame(game);
    }

    @GetMapping(value = "/games")
    public Page<Game> getGames(@RequestParam (value = "page", required = false, defaultValue = "0") int pageNum,
                               @RequestParam (value = "size", required = false, defaultValue = "5") int size) {
        logger.info("[GET] /games");
        PageRequest p = PageRequest.of(pageNum, size);
        return gamesService.getGames(p);
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

    @DeleteMapping(value = "/games/delete/{id}")
    public void deleteGame(@PathVariable Long id)
    {
        logger.info("[DELETE] /game/delete/" + id);
        gamesService.deleteGame(id);
    }


}
