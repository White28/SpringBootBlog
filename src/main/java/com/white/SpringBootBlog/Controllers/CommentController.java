package com.white.SpringBootBlog.Controllers;

import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.white.SpringBootBlog.Models.Comment;
import com.white.SpringBootBlog.Models.User;
import com.white.SpringBootBlog.Repositories.ICommentRepository;
import com.white.SpringBootBlog.Repositories.IUserRepository;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 23, 2017
 *       <p>
 *       Restful API for the controller with simple CRUD methods;
 */
@RestController
@RequestMapping(value = "/post/{id}")
public class CommentController {

	@Autowired
	private ICommentRepository commentRepository;

	@Autowired
	private IUserRepository userRepository;

	/**
	 * 
	 * @param id
	 *            - the id of comment that should be shown;
	 * @return the entity of comment form db;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Comment getOne(@PathVariable(name = "id") ObjectId id) {
		System.out.println(id);
		return commentRepository.findOne(id);
	}

	/**
	 * 
	 * @param comment
	 *            - the model that should be added to database;
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void create(@RequestBody Comment comment) {
		commentRepository.insert(comment);
	}

	/**
	 * 
	 * @param comment
	 *            - comment with fields that should be updated;
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody Comment comment) {
		commentRepository.save(comment);
	}

	/**
	 * 
	 * @param id
	 *            - the id of comment that should be deleted;
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void delete(@PathVariable ObjectId id) {
		commentRepository.delete(id);
	}

	/**
	 * 
	 * @param id
	 *            - the id of post;
	 * @return Collection of users that liked the comment;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/likes")
	public Set<User> getAllUsersWhoLikedComment(@PathVariable(value = "id") ObjectId id) {
		return userRepository.getAllUsers(commentRepository.findOne(id).getSetOfLikes());
	}
}
