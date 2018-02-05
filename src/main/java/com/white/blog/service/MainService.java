package com.white.blog.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.white.blog.model.Comment;
import com.white.blog.model.Post;
import com.white.blog.model.User;
import com.white.blog.repository.ICommentRepository;
import com.white.blog.repository.IPostRepository;
import com.white.blog.repository.IUserRepository;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi 
 * @data Nov 30, 2017
 *       <p>
 */
@org.springframework.stereotype.Service
public class MainService {

	@Autowired
	private ICommentRepository commentRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IPostRepository postRepository;

	/**
	 * Method get comments which are located on the current page
	 * 
	 * @param listOfCommentId
	 *            it takes a collection of comments which were left under the
	 *            post;
	 * @return it gets back a collection of comments which were left under the
	 *         post;
	 */
	public List<Comment> getComments(List<ObjectId> listOfCommentId, int page, int size) {
		List<Comment> listOfComments = new ArrayList<>();
		listOfCommentId.forEach(id -> listOfComments.add(commentRepository.findOne(id)));
		return listOfComments.stream().skip(page * size).limit(size).collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Method get users which are located on the current page
	 * 
	 * @param setOfUserId
	 *            it takes a collection of users id who liked post or comment;
	 * @return it gets back a collection of user who liked post or comment;
	 */
	public Set<User> getUsers(Set<ObjectId> setOfUserId, int page, int size) {
		Set<User> setOfUsers = new LinkedHashSet<>();
		setOfUserId.forEach(id -> setOfUsers.add(userRepository.findOne(id)));
		return setOfUsers.stream().skip(page * size).limit(size).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	/**
	 * Method get posts which are located on the current page
	 * 
	 * @param page
	 *            the number of page;
	 * @param size
	 *            the amount of items that will be shown;
	 * @return a paged and sized collection of items;
	 */
	public List<Post> findAll(int page, int size) {
		Page<Post> records = postRepository.findAll(new PageRequest(page, size));
		return records.getContent();
	}
}
