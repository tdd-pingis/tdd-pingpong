package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long userId;
    private String name;
    private int level;

    @OneToMany(targetEntity=State.class, mappedBy="user", fetch=FetchType.LAZY)
    private List<State> states;

    protected User() {}

    public User(String name, int level) {
        this.name = name;
        this.level = level;
        this.states = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public List<State> getStates() {
        return states;
    }
}
