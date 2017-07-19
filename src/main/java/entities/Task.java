package main.java.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.List;
import java.util.ArrayList;

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