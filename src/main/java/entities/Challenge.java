package main.java.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.ArrayList;

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