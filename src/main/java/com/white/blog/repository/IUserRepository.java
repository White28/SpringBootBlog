package com.white.blog.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.white.blog.model.User;

/**
 * Service layer for UserControoler;
 * <p>
 * 
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 * 
 */
public interface IUserRepository extends MongoRepository<User, ObjectId> {

}
