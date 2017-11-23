package com.white.SpringBootBlog;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.white.SpringBootBlog.Models.Comment;
import com.white.SpringBootBlog.Models.Post;
import com.white.SpringBootBlog.Models.User;
import com.white.SpringBootBlog.Repositories.ICommentRepository;
import com.white.SpringBootBlog.Repositories.IPostRepository;
import com.white.SpringBootBlog.Repositories.IUserRepository;

/**
 * @author Alexander Torchynskyi, Dima Bilyi
 * @data Nov 22, 2017
 *       <p>
 *       Class that contains tests for PostController;
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PostControllerTest {

	private static final String URL_POST = "/post";
	private static final Date DATE_OF_PUBLISHING = new Date();

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private Post post;

	private User user1;

	private User user2;

	private List<Post> postList;

	private List<Comment> listOfComments;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private IPostRepository postRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private ICommentRepository commentRepository;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

		postList = new ArrayList<>();

		this.postRepository.deleteAll();

		this.user1 = userRepository.save(new User("alex", "tor"));
		this.user2 = userRepository.save(new User("dima", "white"));

		this.post = new Post("someTitle", "someBody", new Date(), user1.getId());
		this.post.setId(new ObjectId());

		this.postList.add(postRepository.save(new Post("someTitle", "someBody", new Date(), user1.getId())));
		this.postList.add(postRepository.save(new Post("NewTitle", "NewBody", new Date(), user1.getId())));

	}

	/**
	 * Finds post by its id and checks the body of post if it's the same;
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReadSinglePostSuccess() throws Exception {

		mockMvc.perform(get(URL_POST + "/" + this.post.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(contentType)).andExpect(jsonPath("$.title", is(this.post.getTitle())))
				.andExpect(jsonPath("$.body", is(this.post.getBody())))
				.andExpect(jsonPath("$.tags", is(this.post.getTags())));
	}

	/**
	 * Finds all posts and checks the body of posts if it's the same;
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReadAllPostsSuccess() throws Exception {

		mockMvc.perform(get(URL_POST)).andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].title", is(this.postList.get(0).getTitle())))
				.andExpect(jsonPath("$[0].body", is(this.postList.get(0).getBody())))
				.andExpect(jsonPath("$[1].title", is(this.postList.get(1).getTitle())))
				.andExpect(jsonPath("$[1].body", is(this.postList.get(1).getBody())));
	}

	/**
	 * That test finds a post by its id if the one exists it is deleted, then it
	 * checks if the post is existed in db, supposed to be null;
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeletePostSuccess() throws Exception {
		mockMvc.perform(get(URL_POST + "/" + this.post.getId())).andExpect(status().isOk());
		mockMvc.perform(delete(URL_POST + "/" + this.post.getId())).andExpect(status().isOk());
		assertEquals(postRepository.findOne(post.getId()), null);
	}

	/**
	 * That test creates a new object of post model, then converts it into json
	 * format. After it insert the post into db through create method of
	 * PostController. Then it changes on of the fields and call method update of
	 * the same controller. Last thing it checks if the field was updated in
	 * database;
	 *
	 * @throws Exception
	 */
	@Test
	public void testCreateAndUpdatePostSuccess() throws Exception {
		String postJson = json(post);
		this.mockMvc.perform(post(URL_POST).contentType(MediaType.APPLICATION_JSON).content(postJson))
				.andExpect(status().isOk());
		post.setBody("anyBody");

		mockMvc.perform(put(URL_POST).contentType(MediaType.APPLICATION_JSON).content(json(post)))
				.andExpect(status().isOk());
		assertEquals(post.getBody(), postRepository.findOne(post.getId()).getBody());
	}

	/**
	 * In this test was created a collection of user's ids who liked post, then it
	 * saves that collection into Post model "post" and saves "post" into db. Then
	 * it gets back all users who liked the comment, and checks the fields of users;
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetAllUsersWhoLikedPostSuccess() throws Exception {
		Set<ObjectId> setOfUsersWhoLikedPost = new LinkedHashSet<>();
		setOfUsersWhoLikedPost.add(user1.getId());
		setOfUsersWhoLikedPost.add(user2.getId());

		this.post.setSetOfLikes(setOfUsersWhoLikedPost);
		postRepository.save(this.post);

		mockMvc.perform(get("/post/" + this.post.getId() + "/likes")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$[0].firstName", is(user1.getFirstName())))
				.andExpect(jsonPath("$[0].lastName", is(user1.getLastName())))
				.andExpect(jsonPath("$[1].firstName", is(user2.getFirstName())))
				.andExpect(jsonPath("$[1].lastName", is(user2.getLastName())));
	}

	/**
	 * In that test we create and save two users then we added two comments by them
	 * and attached it to the "post" and save it with that changes. After that it
	 * creates ids collection of comments for the "post". Finally it checks if the
	 * "post" has comments which we added before;
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetAllCommentsUnderPostSuccess() throws Exception {
		listOfComments = new ArrayList<>();
		listOfComments.add(
				commentRepository.save(new Comment(user1.getId(), "The body for comment11111", DATE_OF_PUBLISHING)));
		listOfComments.add(
				commentRepository.save(new Comment(user2.getId(), "The body for comment22222", DATE_OF_PUBLISHING)));

		List<ObjectId> listOfCommentsUnderPost = new ArrayList<>();
		listOfCommentsUnderPost.add(listOfComments.get(0).getId());
		listOfCommentsUnderPost.add(listOfComments.get(1).getId());

		this.post.setListOfComments(listOfCommentsUnderPost);
		postRepository.save(this.post);

		mockMvc.perform(get("/post/" + this.post.getId() + "/comments")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$[0].commentText", is(listOfComments.get(0).getCommentText())))
				.andExpect(jsonPath("$[1].commentText", is(listOfComments.get(1).getCommentText())));
	}

	/**
	 * 
	 * @param o
	 *            - is an instance of class that should be converted to json format;
	 * @return the String that looks like json of current object;
	 * @throws IOException
	 */
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
