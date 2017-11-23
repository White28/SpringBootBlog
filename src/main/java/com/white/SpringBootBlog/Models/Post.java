package com.white.SpringBootBlog.Models;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Alexander Torchynskyi
 * @data Nov 22, 2017
 *       <p>
 *       Class that represent collection of posts;
 */
@Document(collection = "post")
public class Post {

	@Id
	private ObjectId id;
	private String title;
	private String body;
	private Date dateOfPublishing;
	private ObjectId userId;
	private Set<ObjectId> setOfLikes;
	private List<String> tags;
	private List<ObjectId> listOfComments;

	public Post() {
	}

	public Post(String title, String body, Date dateOfPublishing, ObjectId authorId) {
		this.title = title;
		this.body = body;
		this.dateOfPublishing = dateOfPublishing;
		this.userId = authorId;
	}

	public Post(String title, String body, Date dateOfPublishing, ObjectId authorId, List<String> tags) {
		this.title = title;
		this.body = body;
		this.dateOfPublishing = dateOfPublishing;
		this.userId = authorId;
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

	public Date getDateOfPublishing() {
		return dateOfPublishing;
	}

	public void setDateOfPublishing(Date dateOfPublishing) {
		this.dateOfPublishing = dateOfPublishing;
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

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", body=" + body + ", dateOfPublishing=" + dateOfPublishing
				+ ", userId=" + userId + ", setOfLikes=" + setOfLikes + ", tags=" + tags + ", listOfComments="
				+ listOfComments + "]";
	}
	
}