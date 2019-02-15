package com.angrygrizley.rsoilab2new.gamesservice;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import sun.jvm.hotspot.gc.shared.Generation;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String name;

    @Column(name = "genre")
    private String genre;

    @Column(name = "min_num")
    private int min_num;

    @Column(name = "max_num")
    private int max_num;

    @Column(name = "group_num")
    private Long group_num;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }

    public int getMin_num() { return min_num; }

    public void setMin_num(int min_num) { this.min_num = min_num; }

    public int getMax_num() { return max_num; }

    public void setMax_num(int max_num) { this.max_num = max_num; }

    public Long getGroup_num() { return group_num; }

    public void setGroup_num(Long group_num) { this.group_num = group_num; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return min_num == game.min_num &&
                max_num == game.max_num &&
                id.equals(game.id) &&
                name.equals(game.name) &&
                genre.equals(game.genre) &&
                group_num.equals(game.group_num);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, genre, min_num, max_num, group_num);
    }


}
