package com.angrygrizley.rsoilab2new.usersservice;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name="groupNum", columnDefinition = "int default 0")
    private int groupNum;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public int getGroupNum() { return groupNum; }

    public void setGroupNum(int group_num) { this.groupNum = group_num; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", groupNum=" + groupNum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return groupNum == user.groupNum &&
                Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(login, user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, login, groupNum);
    }
}
