package com.white.SpringBootBlog.Repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.white.SpringBootBlog.Models.Comment;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 23, 2017
 *       <p>
 *       Service layer for CommentController;
 */
public interface ICommentRepository extends MongoRepository<Comment, ObjectId> {

	/**
	 * 
	 * @param listOfCommentId
	 *            - it takes a collection of comments which were left under the
	 *            post;
	 * @return - it gets back a collection of comments which were left under the
	 *         post;
	 */
	default List<Comment> getAllComments(List<ObjectId> listOfCommentId) {
		List<Comment> listOfComments = new ArrayList<>();
		listOfCommentId.forEach(id -> listOfComments.add(this.findOne(id)));
		return listOfComments;
	}
}
