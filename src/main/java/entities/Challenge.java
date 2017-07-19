package main.java.domain;

import org.springframework.data.annotation.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.GeneratedValue;
import java.util.List;

@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long challengeId;
	private String description;
	private int level;
	private float rating;

	private List<Task> tasks;

	protected Challenge() {}

	public Challenge(String description) {
		this.description = description;
		this.tasks = new ArrayList<>();
		this.rating = 0;
	}
}