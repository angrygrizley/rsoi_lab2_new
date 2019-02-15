package com.angrygrizley.rsoilab2new.gamesservice;


import com.angrygrizley.RSOI2.gamesservice.GameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GamesServiceImplementation implements GamesService {
    private final GamesRepository gamesRepository;

    @Autowired
    public GamesServiceImplementation(GamesRepository gamesRepository) {this.gamesRepository = gamesRepository; }

    @Override
    public void addGame(Game game) { gamesRepository.save(game); }

    @Override
    public List<Game> getGames() { return gamesRepository.findAll();}

    @Override
    public Game getGameById(Long id) throws GameNotFoundException
    { return gamesRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id)); }

    @Override
    public Game putGame(Game newGame) throws GameNotFoundException {
        return gamesRepository.findById(newGame.getId()).map(Game -> {
            Game.setName(Game.getName());
            Game.setGenre(Game.getGenre());
            Game.setGroup_num(newGame.getGroup_num());
            Game.setMin_num(newGame.getMin_num());
            Game.setMax_num(newGame.getMax_num());
            return gamesRepository.save(Game);
        }).orElseThrow(() -> new GameNotFoundException(newGame.getId()));
    }

    @Override
    public List<Game> searchByGenre(String genre) {
        return gamesRepository.findAllByGenre(genre);
    }

    @Override
    public List<Game> searchByPlayerNum(int num) {
        List<Game> all = gamesRepository.findAll();
        List<Game> result = new ArrayList<>();
        int n = all.size()-1;
        for (int i = 0; i < n; i++)
        {
            if ((all.get(i).getMin_num() <= num) && (all.get(i).getMax_num() >= num))
            {
                result.add(all.get(i));
            }
        }
        return result;
    }
}
