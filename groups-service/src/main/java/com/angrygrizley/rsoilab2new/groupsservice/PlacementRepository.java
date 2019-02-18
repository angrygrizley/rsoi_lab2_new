package com.angrygrizley.rsoilab2new.groupsservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlacementRepository extends JpaRepository <Placement, Long> {
    void deleteByGroupIdAndPlayerId(Long groupId, Long playerId);
    Optional<Placement> findByGroupIdAndPlayerId(Long groupId, Long playerId) throws PlacementNotFoundException;
    void deleteAllByGroupId(Long groupId);
    void deleteById(Long id);
}
