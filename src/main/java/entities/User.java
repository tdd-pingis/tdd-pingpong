package main.java.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.ArrayList;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long userId;
    private String name;
    private int level;

    private List<State> states;

    protected User() {}

    public User(String name, int level) {
        this.name = name;
        this.level = level;
        this.states = new ArrayList<>();
    }
}