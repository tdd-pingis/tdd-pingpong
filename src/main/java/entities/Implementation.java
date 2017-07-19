package main.java.domain;

import org.springframework.data.annotation.Entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.GeneratedValue;

@Entity
public class Implementation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private long implementationId;
	private String code;
	private float rating;

	protected Implementation() {}

	public Implementation(String code) {
		this.code = code;
		this.rating = 0;
	}
}