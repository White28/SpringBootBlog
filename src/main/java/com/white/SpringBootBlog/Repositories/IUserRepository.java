package com.white.SpringBootBlog.Repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.white.SpringBootBlog.Models.User;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 *       <p>
 */
public interface IUserRepository extends MongoRepository<User, ObjectId> {

}
