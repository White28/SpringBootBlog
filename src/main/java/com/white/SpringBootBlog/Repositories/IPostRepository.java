package com.white.SpringBootBlog.Repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.white.SpringBootBlog.Models.Post;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 *       <p>
 */
public interface IPostRepository extends MongoRepository<Post, ObjectId> {

	default List<Post> findAll(int page, int size) {
		Page<Post> records = this.findAll(new PageRequest(page, size));
		return records.getContent();
	}
}
