package com.white.blog.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.white.blog.model.Post;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 *       <p>
 *       Service layer for UserController;
 */
public interface IPostRepository extends MongoRepository<Post, ObjectId> {

	/**
	 * 
	 * @param page
	 *            - the number of page;
	 * @param size
	 *            - the amount of items that will be shown;
	 * @return a paged and sized collection of items;
	 */
	default List<Post> findAll(int page, int size) {
		Page<Post> records = this.findAll(new PageRequest(page, size));
		return records.getContent();
	}
}
