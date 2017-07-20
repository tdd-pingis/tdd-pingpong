package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class State {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long stateId;
    private String status;

    protected State(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}