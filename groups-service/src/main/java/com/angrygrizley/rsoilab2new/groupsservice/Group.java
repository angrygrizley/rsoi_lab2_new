package com.angrygrizley.rsoilab2new.groupsservice;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String group_name;

    @Column(name = "free_space")
    private int free_space;

    @Column(name = "game_id")
    private int game_id;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Placement> players;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getGroup_name() { return group_name; }

    public void setGroup_name(String group_name) { this.group_name = group_name; }

    public int getFree_space() { return free_space; }

    public void setFree_space(int free_space) { this.free_space = free_space; }

    public int getGame_id() { return game_id; }

    public void setGame_id(int game_id) { this.game_id = game_id; }

    public List<Placement> getPlayers() { return players; }

    public void setPlayers(List<Placement> players) { this.players = players; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return free_space == group.free_space &&
                game_id == group.game_id &&
                id.equals(group.id) &&
                group_name.equals(group.group_name) &&
                players.equals(group.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, group_name, free_space, game_id, players);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", group_name='" + group_name + '\'' +
                ", free_space=" + free_space +
                ", game_id=" + game_id +
                ", players=" + players +
                '}';
    }
}
