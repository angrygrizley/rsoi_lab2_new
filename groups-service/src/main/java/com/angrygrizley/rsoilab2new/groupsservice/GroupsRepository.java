package com.angrygrizley.rsoilab2new.groupsservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupsRepository extends JpaRepository <Group, Long> {
    List<Group> findAllByFree_spaceIsNotNull();
    List<Group> findAllByGame_id(Long id);
}
