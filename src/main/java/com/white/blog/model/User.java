package com.white.blog.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class that represent collection of users, each post and comment have an
 * author (user model);
 * <p>
 * 
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 * 
 */
@Document(collection = "user")
public class User {

	@Id
	private ObjectId id;
	private String firstName;
	private String lastName;

	public User() {
	}

	public User(String firstName, String lastName) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
