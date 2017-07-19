package main.java.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long testId;
	private String code;
	private float rating;

	protected Test() {}

	public Test(String code) {
		this.code = code;
		this.rating = 0;
	}
}