package main.java.domain;

import org.springframework.data.annotation.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.GeneratedValue;

@Entity
public class State {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long stateId;
	private String status;

	protected State(String status) {
		this.status = status;
	}
}