package com.angrygrizley.rsoilab2new.groupsservice;

public class NoFreeSpaceException extends Exception {
    public NoFreeSpaceException(Long groupID) {
        super("Group with id = " + groupID + " does not have free spaces!");
    }
}
