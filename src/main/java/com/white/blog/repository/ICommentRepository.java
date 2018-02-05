package com.white.blog.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.white.blog.model.Comment;

/**
 * Service layer for CommentController;
 * <p>
 * 
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 23, 2017
 * 
 */
public interface ICommentRepository extends MongoRepository<Comment, ObjectId> {

}
