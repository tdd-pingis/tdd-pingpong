package main.java.domain;

import org.springframework.data.annotation.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.GeneratedValue;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long userId;
	private String name;
	private int level;

	private List<State> states;

	protected User() {}

	public User(String name, int level) {
		this.name = name;
		this.level = level;
		this.states = new ArrayList<>();
	}
}