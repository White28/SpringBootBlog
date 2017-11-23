package com.white.SpringBootBlog.Repositories;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.white.SpringBootBlog.Models.User;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 *       <p>
 */
public interface IUserRepository extends MongoRepository<User, ObjectId> {

	default Set<User> getAllUsers(Set<ObjectId> setOfUserId) {
		Set<User> setOfUsers = new LinkedHashSet<>();

		for (ObjectId id : setOfUserId) {
			setOfUsers.add(this.findOne(id));
		}
		return setOfUsers;
	}
}
