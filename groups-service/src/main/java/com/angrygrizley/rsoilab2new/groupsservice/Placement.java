package com.angrygrizley.rsoilab2new.groupsservice;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "placements")
public class Placement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="groupId")
    private Long groupId;

    @Column(name="playerId")
    private Long playerId;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getGroupId() { return groupId; }

    public void setGroupId(Long groupId) { this.groupId = groupId; }

    public Long getPlayerId() { return playerId; }

    public void setPlayerId(Long playerId) { this.playerId = playerId; }

    public Placement() {};


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placement placement = (Placement) o;
        return id.equals(placement.id) &&
                groupId.equals(placement.groupId) &&
                playerId.equals(placement.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, playerId);
    }

    @Override
    public String toString() {
        return "Placement{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", playerId=" + playerId +
                '}';
    }

    public Placement(Long groupId, Long playerId) {
        this.groupId = groupId;
        this.playerId = playerId;
    }

}

