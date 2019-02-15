package com.angrygrizley.rsoilab2new.gamesservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamesRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByGenre(String genre);
}
