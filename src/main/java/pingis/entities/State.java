package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

@Entity
public class State {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long stateId;
    private String status;
    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    protected State(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
