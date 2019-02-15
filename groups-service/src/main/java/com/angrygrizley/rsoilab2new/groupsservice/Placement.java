package com.angrygrizley.rsoilab2new.groupsservice;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "placements")
public class Placement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="group_id")
    private Long group_id;

    @Column(name="player_id")
    private Long player_id;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getGroup_id() { return group_id; }

    public void setGroup_id(Long group_id) { this.group_id = group_id; }

    public Long getPlayer_id() { return player_id; }

    public void setPlayer_id(Long player_id) { this.player_id = player_id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Placement placement = (Placement) o;
        return id.equals(placement.id) &&
                group_id.equals(placement.group_id) &&
                player_id.equals(placement.player_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group_id, player_id);
    }

    @Override
    public String toString() {
        return "Placement{" +
                "id=" + id +
                ", group_id=" + group_id +
                ", player_id=" + player_id +
                '}';
    }

    public Placement(Long group_id, Long player_id) {
        this.group_id = group_id;
        this.player_id = player_id;
    }

}

