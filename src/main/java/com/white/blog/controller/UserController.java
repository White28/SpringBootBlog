package com.white.blog.controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.white.blog.model.User;
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
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserRepository userRepository;

	/**
	 * Find the user by id
	 * 
	 * @param id
	 *            the id of user that should be shown;
	 * @return the entity of user form db;
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public User getById(@PathVariable(name = "id") ObjectId id) {
		return userRepository.findOne(id);
	}

	/**
	 * Insert a user into database
	 * 
	 * @param user
	 *            the model that should be added to database;
	 */
	@RequestMapping(method = RequestMethod.POST)
	public void create(@RequestBody User user) {
		userRepository.insert(user);
	}

	/**
	 * Update the user in database
	 * 
	 * @param user
	 *            user with fields that should be updated;
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody User user) {
		userRepository.save(user);
	}

	/**
	 * Delete the user by id
	 * 
	 * @param id
	 *            the id of user that should be deleted;
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void delete(@PathVariable ObjectId id) {
		userRepository.delete(id);
	}
}
