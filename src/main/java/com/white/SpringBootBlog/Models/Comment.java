package com.white.SpringBootBlog.Models;

import java.util.Date;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Alexander Torchynskyi
 * @data Nov 22, 2017
 *       <p>
 */
@Document(collection = "comment")
public class Comment {

	@Id
	private ObjectId id;
	private ObjectId userId;
	private String commentText;
	private Date dateOfPublishing;
	private Set<ObjectId> setOfLikes;

	public Comment() {
	}

	public Comment(ObjectId userId, String commentText, Date dateOfPublishing) {
		this.userId = userId;
		this.commentText = commentText;
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

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
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
