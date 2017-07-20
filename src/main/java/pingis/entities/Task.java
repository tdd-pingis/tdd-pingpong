package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long taskId;
    private String code;
    private float rating;
    @ManyToOne(fetch=FetchType.LAZY)
    private Challenge challenge;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="task")
    private List<Implementation> implementations;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="task")
    private List<Test> tests;

    protected Task() {}

    public Task(String code) {
        this.code = code;
        implementations = new ArrayList<>();
        this.rating = 0;
    }
}