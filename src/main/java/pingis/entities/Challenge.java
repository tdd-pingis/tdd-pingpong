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
public class Challenge {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long challengeId;
    private String name;
    private String description;
    private int level;
    private float rating;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="challenge")
    private List<Task> tasks;

    protected Challenge() {}

    public Challenge(String name, String description) {
        this.description = description;
        this.name = name;
        this.tasks = new ArrayList<>();
        this.rating = 0;
    }

}