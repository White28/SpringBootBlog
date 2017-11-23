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

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public User getOne(@PathVariable(name = "id") ObjectId id) {
		return userRepository.findOne(id);
	}

	@RequestMapping(method = RequestMethod.POST)
	public void save(@RequestBody User user) {
		userRepository.insert(user);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public void update(@RequestBody User user) {
		userRepository.save(user);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void delete(@PathVariable ObjectId id) {
		userRepository.delete(id);
	}
}
