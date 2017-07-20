package pingis.entities;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long testId;
    private String code;
    private float rating;
    @ManyToOne(fetch=FetchType.LAZY)
    private Task task;

    protected Test() {}

    public Test(String code) {
        this.code = code;
        this.rating = 0;
    }

    public String getCode() {
        return code;
    }

    public float getRating() {
        return rating;
    }
}
