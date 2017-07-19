package main.java.domain;

import org.springframework.data.annotation.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.GeneratedValue;

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
	}v
}