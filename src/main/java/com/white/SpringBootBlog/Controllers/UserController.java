package com.white.SpringBootBlog.Controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.white.SpringBootBlog.Models.User;
import com.white.SpringBootBlog.Repositories.IUserRepository;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 *       <p>
 *       Restful API for the controller with simple CRUD methods;
 * 
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserRepository userRepository;

	/**
	 * 
	 * @param id
	 *            - the id of user that should be shown;
	 * @return the entity of user form db;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public User getOne(@PathVariable(name = "id") ObjectId id) {
		return userRepository.findOne(id);
	}

	/**
	 * 
	 * @param user
	 *            - the model that should be added to database;
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void create(@RequestBody User user) {
		userRepository.insert(user);
	}

	/**
	 * 
	 * @param user
	 *            - user with fields that should be updated;
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody User user) {
		userRepository.save(user);
	}

	/**
	 * 
	 * @param id
	 *            - the id of user that should be deleted;
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void delete(@PathVariable ObjectId id) {
		userRepository.delete(id);
	}
}
