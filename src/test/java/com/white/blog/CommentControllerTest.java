package com.white.blog;

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

import com.white.blog.model.Comment;
import com.white.blog.model.Post;
import com.white.blog.model.User;
import com.white.blog.repository.ICommentRepository;
import com.white.blog.repository.IPostRepository;
import com.white.blog.repository.IUserRepository;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 23, 2017
 *       <p>
 *       Class that contains tests for CommentController;
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class CommentControllerTest {
	private static final String URL_POST = "/post/";
	private static final Date DATE_OF_PUBLISHING = new Date();

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private Post post;

	private Comment comment;

	private List<User> listOfUsers;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private ICommentRepository commentRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IPostRepository postRepository;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);

		assertNotNull("the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter);
	}

	@Before
	public void setUp() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		commentRepository.deleteAll();

		listOfUsers = new ArrayList<>();

		listOfUsers.add(userRepository.save(new User("Alex", "Tor")));
		listOfUsers.add(userRepository.save(new User("Dimka", "Bilyi")));

		this.post = postRepository
				.save(new Post("someTitle", "someBody", DATE_OF_PUBLISHING, listOfUsers.get(0).getId()));

		this.comment = commentRepository
				.save(new Comment(listOfUsers.get(0).getId(), "The body for comment", DATE_OF_PUBLISHING));
	}

	/**
	 * Finds comment by its id and checks the body of comment if it's the same;
	 * 
	 * @throws Exception
	 */
	@Test
	public void testReadSingleCommentSuccess() throws Exception {
		mockMvc.perform(get(URL_POST + this.post.getId() + "/" + comment.getId())).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.commentText", is(this.comment.getCommentText())));
	}

	/**
	 * That test finds a comment by its id if the one exists it is deleted, then it
	 * checks if the comment is existed in db, supposed to be null;
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDeleteCommentSuccess() throws Exception {
		mockMvc.perform(get(URL_POST + this.post.getId() + "/" + comment.getId())).andExpect(status().isOk());
		mockMvc.perform(delete(URL_POST + this.post.getId() + "/" + comment.getId())).andExpect(status().isOk());
		assertEquals(commentRepository.findOne(comment.getId()), null);
	}

	/**
	 * That test creates a new object of comment model, then converts it into json
	 * format. After it insert the comment into db through create method of
	 * CommentController. Then it changes on of the fields and call method update of
	 * the same controller. Last thing it checks if the field was updated in
	 * database;
	 *
	 * @throws Exception
	 */
	@Test
	public void testCreateAndUpdateCommentSuccess() throws Exception {
		comment.setId(new ObjectId());

		String commentsJson = json(comment);

		this.mockMvc.perform(
				post(URL_POST + post.getId() + "/").contentType(MediaType.APPLICATION_JSON).content(commentsJson))
				.andExpect(status().isOk());
		comment.setCommentText("Another comment");

		mockMvc.perform(
				put(URL_POST + post.getId() + "/").contentType(MediaType.APPLICATION_JSON).content(json(comment)))
				.andExpect(status().isOk());
		assertEquals(comment.getCommentText(), commentRepository.findOne(comment.getId()).getCommentText());
	}

	/**
	 * In this test was created a collection of user's ids who liked comment, then
	 * it saves that collection into Comment model "comment" and saves "comment"
	 * into db. Then it gets back all users who liked the comment, and checks the
	 * fields of users;
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetAllUsersWhoLikedCommentSuccess() throws Exception {
		Set<ObjectId> setOfUsersWhoLikedComment = new LinkedHashSet<>();
		setOfUsersWhoLikedComment.add(listOfUsers.get(0).getId());
		setOfUsersWhoLikedComment.add(listOfUsers.get(1).getId());

		this.comment.setSetOfLikes(setOfUsersWhoLikedComment);
		commentRepository.save(this.comment);

		mockMvc.perform(get("/post/" + this.post.getId() + "/" + comment.getId() + "/likes")).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$[0].firstName", is(listOfUsers.get(0).getFirstName())))
				.andExpect(jsonPath("$[0].lastName", is(listOfUsers.get(0).getLastName())))
				.andExpect(jsonPath("$[1].firstName", is(listOfUsers.get(1).getFirstName())))
				.andExpect(jsonPath("$[1].lastName", is(listOfUsers.get(1).getLastName())));
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
