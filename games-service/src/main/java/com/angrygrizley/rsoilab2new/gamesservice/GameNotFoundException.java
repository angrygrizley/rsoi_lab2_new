package com.angrygrizley.rsoilab2new.gamesservice;

public class GameNotFoundException extends Exception {
    public GameNotFoundException(Long id){
        super("Game with id= " + id + " not found");
    }
}
