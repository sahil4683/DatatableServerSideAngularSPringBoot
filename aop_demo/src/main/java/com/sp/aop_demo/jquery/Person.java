package com.sp.aop_demo.jquery;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "person")
@Data
public class Person {

	@Id
	private Long id;

	private String name;

	private String place;

	private String city;

	private String state;

	private String phone;

	// Constructors, getters, and setters
}
