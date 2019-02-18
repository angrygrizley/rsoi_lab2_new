package com.angrygrizley.rsoilab2new.gamesservice;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "genre")
    private String genre;

    @Column(name = "minNum")
    private int minNum;

    @Column(name = "maxNum")
    private int maxNum;

    @Column(name = "groupNum")
    private Long groupNum;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }

    public int getMinNum() { return minNum; }

    public void setMinNum(int minNum) { this.minNum = minNum; }

    public int getMaxNum() { return maxNum; }

    public void setMaxNum(int maxNum) { this.maxNum = maxNum; }

    public Long getGroupNum() { return groupNum; }

    public void setGroupNum(Long groupNum) { this.groupNum = groupNum; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return minNum == game.minNum &&
                maxNum == game.maxNum &&
                id.equals(game.id) &&
                title.equals(game.title) &&
                genre.equals(game.genre) &&
                groupNum.equals(game.groupNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, genre, minNum, maxNum, groupNum);
    }


}
