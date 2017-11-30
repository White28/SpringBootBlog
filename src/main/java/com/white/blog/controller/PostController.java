package com.white.blog.controller;

import java.util.List;
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
import com.white.blog.model.Post;
import com.white.blog.model.User;
import com.white.blog.repository.ICommentRepository;
import com.white.blog.repository.IPostRepository;
import com.white.blog.repository.IUserRepository;

/**
 * Restful API for the controller with simple CRUD methods;
 * <p>
 * 
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 * 
 */
@RestController
@RequestMapping(value = "/post")
public class PostController {

	private static final String DEFAULT_PAGE = "0";
	private static final String DEFAULT_SIZE = "5";

	@Autowired
	private IPostRepository postRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private ICommentRepository commentRepository;

	/**
	 * Insert a post into database
	 * 
	 * @param post
	 *            the model that should be added to database;
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void create(@RequestBody Post post) {
		postRepository.insert(post);
	}

	/**
	 * Find all posts on some page
	 * 
	 * @param page
	 *            is number of page;
	 * 
	 * @param size
	 *            is amount of elements displayed per page;
	 * 
	 * @return List<Post> a paged list of posts;
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Post> getAll(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
		return postRepository.findAll(page, size);
	}

	/**
	 * Find the post by id
	 * 
	 * @param id
	 *            the id of post that should be shown;
	 * @return the entity of post form db;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Post getById(@PathVariable(value = "id") ObjectId id) {
		return postRepository.findOne(id);
	}

	/**
	 * Delete the post by id
	 * 
	 * @param id
	 *            the id of post that should be deleted;
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void delete(@PathVariable(value = "id") ObjectId id) {
		postRepository.delete(id);
	}

	/**
	 * Update the post in database
	 * 
	 * @param post
	 *            with fields that should be updated;
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody Post post) {
		postRepository.save(post);
	}

	/**
	 * Find users who liked or disliked the post
	 * 
	 * @param id
	 *            the id of post;
	 * @return return Collection of users that liked or disliked the post;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/likes")
	public Set<User> getUsersByActivity(@PathVariable(value = "id") ObjectId id) {
		return userRepository.getAllUsers(postRepository.findOne(id).getSetOfLikes());
	}

	/**
	 * Find comments which were left under the post
	 * 
	 * @param id
	 *            the id of post;
	 * @return return Collection of comments which were left under the post
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/comments")
	public List<Comment> getComments(@PathVariable(value = "id") ObjectId id) {
		return commentRepository.getAllComments(postRepository.findOne(id).getListOfComments());
	}
}
