package com.angrygrizley.RSOI2.gamesservice;

public class GameNotFoundException extends Exception {
    public GameNotFoundException(Long id){
        super("Game with id= " + id + " not found");
    }
}
