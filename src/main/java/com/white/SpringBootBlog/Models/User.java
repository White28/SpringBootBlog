package com.white.SpringBootBlog.Models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Alexander Torchynskyi
 * @data Nov 22, 2017
 *       <p>
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
