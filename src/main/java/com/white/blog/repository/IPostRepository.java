package com.white.blog.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.white.blog.model.Post;

/**
 * Service layer for UserController;
 * <p>
 * 
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 * 
 */
public interface IPostRepository extends MongoRepository<Post, ObjectId> {

}
