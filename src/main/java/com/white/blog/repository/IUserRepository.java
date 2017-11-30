package com.white.blog.repository;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.white.blog.model.User;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 *       <p>
 *       Service layer for UserControoler;
 * 
 */
public interface IUserRepository extends MongoRepository<User, ObjectId> {

	/**
	 * 
	 * @param setOfUserId
	 *            - it takes a collection of users id who liked post or comment;
	 * @return it gets back a collection of user who liked post or comment;
	 */
	default Set<User> getAllUsers(Set<ObjectId> setOfUserId) {
		Set<User> setOfUsers = new LinkedHashSet<>();
		setOfUserId.forEach(id -> setOfUsers.add(this.findOne(id)));
		return setOfUsers;
	}
}
