package main.java.domain;

import org.springframework.data.annotation.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.GeneratedValue;
import java.util.List;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long taskId;
	private String code;
	private float rating;

	private List<Implementation> implementations;
	private List<Test> tests;

	protected Task() {}

	public Task(String code) {
		this.code = code;
		implementations = new ArrayList<>();
		this.rating = 0;
	}
}