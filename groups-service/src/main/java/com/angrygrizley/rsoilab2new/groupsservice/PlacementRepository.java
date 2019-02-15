package com.angrygrizley.rsoilab2new.groupsservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlacementRepository extends JpaRepository <Placement, Long> {
    void deleteByGroup_idAndPlayer_id(Long groupId, Long playerId);
    Optional<Placement> findByGroup_idAndPlayer_id(Long groupId, Long playerId) throws PlacementNotFoundException;
}
