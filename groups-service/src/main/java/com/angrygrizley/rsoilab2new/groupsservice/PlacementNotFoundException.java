package com.angrygrizley.rsoilab2new.groupsservice;


public class PlacementNotFoundException extends Exception {
    public PlacementNotFoundException (Long groupId, Long playerId){
        super("Player with id = " + playerId + " is not in group with id = " + groupId);
    }
}
