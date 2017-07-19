package main.java.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

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