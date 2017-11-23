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

import com.white.SpringBootBlog.Models.User;
import com.white.SpringBootBlog.Repositories.IUserRepository;

/**
 * @author Alexander Torchynskyi, Dmytro Bilyi
 * @data Nov 22, 2017
 *       <p>
 *       Restful API for the controller with simple CRUD methods;
 * 
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserControllerTest {
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private User user;

	private List<User> userList;

	@Autowired
	private WebApplicationContext webApplicationContext;

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

		userList = new ArrayList<>();

		this.userRepository.deleteAll();

		this.userList.add(userRepository.save(new User("FirstName1", "LastName1")));

		this.userList.add(userRepository.save(new User("FirstName2", "LastName2")));
	}

	@Test
	public void readSingleUserSuccess() throws Exception {
		mockMvc.perform(get("/user/" + this.userList.get(0).getId())).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.firstName", is(this.userList.get(0).getFirstName())))
				.andExpect(jsonPath("$.lastName", is(this.userList.get(0).getLastName())));
	}

	@Test
	public void deleteUserSuccess() throws Exception {
		mockMvc.perform(get("/user/" + this.userList.get(0).getId())).andExpect(status().isOk());
		mockMvc.perform(delete("/user/" + this.userList.get(0).getId())).andExpect(status().isOk());
		assertEquals(userRepository.findOne(userList.get(0).getId()), null);
	}

	@Test
	public void CreateAndUpdateUserSuccess() throws Exception {
		this.user = new User("FirstName123", "LastName123");
		user.setId(new ObjectId());
		String postJson = json(user);
		this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(postJson))
				.andExpect(status().isOk());
		user.setFirstName("AnotherFirstName");

		mockMvc.perform(put("/user").contentType(MediaType.APPLICATION_JSON).content(json(user)))
				.andExpect(status().isOk());
		assertEquals(user.getFirstName(), userRepository.findOne(user.getId()).getFirstName());
	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
