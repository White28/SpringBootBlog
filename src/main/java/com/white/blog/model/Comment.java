package com.white.blog.model;

import java.util.Date;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class that represent collection comment, each post might have list of them;
 * <p>
 * 
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 * 
 */
@Document(collection = "comment")
public class Comment {

	@Id
	private ObjectId id;
	private ObjectId userId;
	private String body;
	private Date dateOfPublishing;
	private Set<ObjectId> setOfLikes;

	public Comment() {
	}

	public Comment(ObjectId userId, String commentText, Date dateOfPublishing) {
		this();
		this.userId = userId;
		this.body = commentText;
		this.dateOfPublishing = dateOfPublishing;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDateOfPublishing() {
		return dateOfPublishing;
	}

	public void setDateOfPublishing(Date dateOfPublishing) {
		this.dateOfPublishing = dateOfPublishing;
	}

	public Set<ObjectId> getSetOfLikes() {
		return setOfLikes;
	}

	public void setSetOfLikes(Set<ObjectId> setOfLikes) {
		this.setOfLikes = setOfLikes;
	}
}
