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

    @Column(name = "groupName")
    private String groupName;

    @Column(name = "freeSpace")
    private int freeSpace;

    @Column(name = "gameId")
    private int gameId;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private List<Placement> players;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getGroupName() { return groupName; }

    public void setGroupName(String groupName) { this.groupName = groupName; }

    public int getFreeSpace() { return freeSpace; }

    public void setFreeSpace(int freeSpace) { this.freeSpace = freeSpace; }

    public int getGameId() { return gameId; }

    public void setGameId(int gameId) { this.gameId = gameId; }

    public List<Placement> getPlayers() { return players; }

    public void setPlayers(List<Placement> players) { this.players = players; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return freeSpace == group.freeSpace &&
                gameId == group.gameId &&
                id.equals(group.id) &&
                groupName.equals(group.groupName) &&
                players.equals(group.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, freeSpace, gameId, players);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", freeSpace=" + freeSpace +
                ", gameId=" + gameId +
                ", players=" + players +
                '}';
    }
}
