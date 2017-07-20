package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

@Entity
public class Implementation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long implementationId;
    private String code;
    private float rating;

    @ManyToOne(fetch=FetchType.LAZY)
    private Task task;

    protected Implementation() {}

    public Implementation(String code) {
        this.code = code;
        this.rating = 0;
    }
}
