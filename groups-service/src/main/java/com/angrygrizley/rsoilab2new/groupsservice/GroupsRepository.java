package com.angrygrizley.rsoilab2new.groupsservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupsRepository extends JpaRepository <Group, Long> {

    @Query("select g from #{#entityName} g where g.freeSpace > 0")
    List<Group> findAllGroupsByFreeSpace();

    List<Group> findAllByGameId(Long id);
}
