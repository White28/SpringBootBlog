package com.white.SpringBootBlog.Controllers;

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

import com.white.SpringBootBlog.Models.Post;
import com.white.SpringBootBlog.Models.User;
import com.white.SpringBootBlog.Repositories.IPostRepository;
import com.white.SpringBootBlog.Repositories.IUserRepository;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 *       <p>
 *       Restful API for the controller with simple CRUD methods;
 * 
 */
@RestController
@RequestMapping(value = "/post")
public class PostController {
	@Autowired
	private IPostRepository postRepository;

	@Autowired
	private IUserRepository userRepository;

	private final String DEFAULT_PAGE = "0";
	private final String DEFAULT_SIZE = "5";

	/**
	 * 
	 * @param post
	 *            - the model that should be added to database;
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void create(@RequestBody Post post) {
		postRepository.insert(post);
	}

	/**
	 * 
	 * @param page
	 *            is number of page;
	 * 
	 * @param size
	 *            is amount of elements displayed per page;
	 * 
	 * @return List<Post> a paged list of posts ;
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Post> getAll(@RequestParam(value = "page", defaultValue = DEFAULT_PAGE) int page,
			@RequestParam(value = "size", defaultValue = DEFAULT_SIZE) int size) {
		return postRepository.findAll(page, size);
	}

	/**
	 * 
	 * @param id
	 *            - the id of post that should be shown;
	 * @return the entity of post form db;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Post getOne(@PathVariable(value = "id") ObjectId id) {
		return postRepository.findOne(id);
	}

	/**
	 * 
	 * @param id
	 *            - the id of post that should be deleted;
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void delete(@PathVariable(value = "id") ObjectId id) {
		postRepository.delete(id);
	}

	/**
	 * 
	 * @param post
	 *            - post with fields that should be updated;
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody Post post) {
		postRepository.save(post);
	}

	/**
	 * 
	 * @param id
	 *            - the id of post;
	 * @return return Collection of users that liked the post;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/likes")
	public Set<User> getAllUsersWhoLikedPost(@PathVariable(value = "id") ObjectId id) {
		return userRepository.getAllUsers(postRepository.findOne(id).getSetOfLikes());
	}
}
