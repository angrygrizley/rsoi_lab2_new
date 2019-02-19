package com.angrygrizley.rsoilab2new.groupsservice;

public class GroupNotFoundException extends Exception{
    public GroupNotFoundException(Long id){
        super("Group with id= " + id + " not found");
    }
}
