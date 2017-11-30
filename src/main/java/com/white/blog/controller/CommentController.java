package com.white.blog.controller;

import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.white.blog.model.Comment;
import com.white.blog.model.User;
import com.white.blog.repository.ICommentRepository;
import com.white.blog.service.MainService;

/**
 * Restful API for the controller with simple CRUD methods;
 * <p>
 * 
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 23, 2017
 * 
 */
@RestController
@RequestMapping(value = "/post/{id}/comment")
public class CommentController {

	private static final String DEFAULT_PAGE = "0";
	private static final String DEFAULT_SIZE = "5";

	@Autowired
	private ICommentRepository commentRepository;

	@Autowired
	private MainService service;

	/**
	 * Find the comment by id
	 * 
	 * @param id
	 *            the id of comment that should be shown;
	 * @return the entity of comment form db;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Comment getById(@PathVariable(name = "id") ObjectId id) {
		return commentRepository.findOne(id);
	}

	/**
	 * Insert a comment into database
	 * 
	 * @param comment
	 *            the model that should be added to database;
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void create(@RequestBody Comment comment) {
		commentRepository.insert(comment);
	}

	/**
	 * Update the comment in database
	 * 
	 * @param comment
	 *            with fields that should be updated;
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody Comment comment) {
		commentRepository.save(comment);
	}

	/**
	 * Delete the comment by id
	 * 
	 * @param id
	 *            the id of comment that should be deleted;
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void delete(@PathVariable ObjectId id) {
		commentRepository.delete(id);
	}

	/**
	 * Find users who liked or disliked the comment
	 * 
	 * @param id
	 *            the id of post;
	 * @return Collection of users that liked or disliked the comment;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/likes")
	public Set<User> getUsersByActivity(@PathVariable(value = "id") ObjectId id,
			@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
		return service.getUsers(commentRepository.findOne(id).getSetOfLikes(), page, size);
	}
}
