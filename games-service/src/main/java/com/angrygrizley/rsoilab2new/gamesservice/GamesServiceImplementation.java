package com.angrygrizley.rsoilab2new.gamesservice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GamesServiceImplementation implements GamesService {
    private final GamesRepository gamesRepository;

    @Autowired
    public GamesServiceImplementation(GamesRepository gamesRepository) {this.gamesRepository = gamesRepository; }

    @Override
    public Game addGame(Game game) { return gamesRepository.save(game); }

    @Override
    public Page<Game> getGames(PageRequest p) { return gamesRepository.findAll(p);}

    @Override
    public Game getGameById(Long id) throws GameNotFoundException
    { return gamesRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id)); }

    @Override
    public Game putGame(Game newGame) throws GameNotFoundException {
        Game old = gamesRepository.findById(newGame.getId()).orElseThrow(() -> new GameNotFoundException(newGame.getId()));
        old.setTitle(newGame.getTitle());
        old.setGenre(newGame.getGenre());
        old.setGroupNum(newGame.getGroupNum());
        old.setMinNum(newGame.getMinNum());
        old.setMaxNum(newGame.getMaxNum());
        return gamesRepository.save(old);
    }


    @Override
    public List<Game> searchGames(String genre, int num) {

        List<Game> result = new ArrayList<Game>();
        if ((genre.isEmpty()) && (num != -1))
            result = gamesRepository.findAllByPlNumBetweenMinNumAnAndMaxNum(num);
        if ((num == -1) && (!genre.isEmpty()))
            result = gamesRepository.findAllByGenre(genre);
        if ((num != -1) && (!genre.isEmpty()))
            result = gamesRepository.findAllByPlNumBetweenMinNumAnAndMaxNumAndGenre(genre, num);
        return result;
    }

    @Override
    public void deleteGame(Long id) { gamesRepository.deleteById(id);}
}
