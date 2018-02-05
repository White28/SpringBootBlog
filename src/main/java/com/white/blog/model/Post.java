package com.white.blog.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Class that represent collection of posts;
 * <p>
 * 
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 * 
 */
@Document(collection = "post")
public class Post {

	@Id
	private ObjectId id;
	private ObjectId userId;
	private String title;
	private String body;
	private Date creationDate;

	private Set<ObjectId> setOfLikes;
	private List<String> tags;
	private List<ObjectId> listOfComments;

	public Post() {
	}

	public Post(String title, String body, Date creationDate, ObjectId authorId) {
		this();
		this.title = title;
		this.body = body;
		this.creationDate = creationDate;
		this.userId = authorId;
	}

	public Post(String title, String body, Date creationDate, ObjectId authorId, List<String> tags) {
		this(title, body, creationDate, authorId);
		this.tags = tags;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public ObjectId getAuthorId() {
		return userId;
	}

	public void setAuthorId(ObjectId authorId) {
		this.userId = authorId;
	}

	public Set<ObjectId> getSetOfLikes() {
		return setOfLikes;
	}

	public void setSetOfLikes(Set<ObjectId> setOfLikes) {
		this.setOfLikes = setOfLikes;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<ObjectId> getListOfComments() {
		return listOfComments;
	}

	public void setListOfComments(List<ObjectId> listOfComments) {
		this.listOfComments = listOfComments;
	}
}