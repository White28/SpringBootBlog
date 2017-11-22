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
import java.util.List;

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

import com.white.SpringBootBlog.Models.Post;
import com.white.SpringBootBlog.Models.User;
import com.white.SpringBootBlog.Repositories.IPostRepository;
import com.white.SpringBootBlog.Repositories.IUserRepository;



/**
 * @author  Alexander Torchynskyi, Dima Bilyi
 * @data Nov 22, 2017
 * <p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class PostControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private Post post;

	private User user;

	private List<Post> postList;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private IPostRepository postRepository;

	@Autowired
	private IUserRepository userRepository;

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

		user = userRepository.save(new User("alex", "tor"));

		this.postList.add(postRepository.save(new Post("someTitle", "someBody", new Date(), user.getId())));

		this.postList.add(postRepository.save(new Post("NewTitle", "NewBody", new Date(), user.getId())));

	}

	@Test
	public void readSinglePostSuccess() throws Exception {

		mockMvc.perform(get("/post/" + this.postList.get(0).getId())).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.title", is(this.postList.get(0).getTitle())))
				.andExpect(jsonPath("$.body", is(this.postList.get(0).getBody())))
				.andExpect(jsonPath("$.tags", is(this.postList.get(0).getTags())));
	}

	@Test
	public void readAllPostsSuccess() throws Exception {

		mockMvc.perform(get("/post")).andExpect(status().isOk()).andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].title", is(this.postList.get(0).getTitle())))
				.andExpect(jsonPath("$[0].body", is(this.postList.get(0).getBody())))
				.andExpect(jsonPath("$[1].title", is(this.postList.get(1).getTitle())))
				.andExpect(jsonPath("$[1].body", is(this.postList.get(1).getBody())));
	}

	@Test
	public void deletePostSuccess() throws Exception {
		mockMvc.perform(get("/post/" + this.postList.get(0).getId())).andExpect(status().isOk());
		mockMvc.perform(delete("/post/" + this.postList.get(0).getId())).andExpect(status().isOk());
		assertEquals(postRepository.findOne(postList.get(0).getId()), null);
	}

	@Test
	public void CreateAndUpdatePostSuccess() throws Exception {
		this.post = new Post("MyTitlke", "someBody", new Date(), user.getId());
		post.setId(new ObjectId());
		String postJson = json(post);
		this.mockMvc
				.perform(
						post("/post").contentType(MediaType.APPLICATION_JSON).content(postJson))
				.andExpect(status().isOk());
		post.setBody("anyBody");

		mockMvc.perform(put("/post").contentType(MediaType.APPLICATION_JSON).content(json(post)))
				.andExpect(status().isOk());
		assertEquals(post.getBody(), postRepository.findOne(post.getId()).getBody());
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
