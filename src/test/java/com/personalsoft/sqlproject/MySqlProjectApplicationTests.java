package com.personalsoft.sqlproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalsoft.sqlproject.controller.UserController;
import com.personalsoft.sqlproject.model.db.UserEntity;
import com.personalsoft.sqlproject.model.dto.UserDto;
import com.personalsoft.sqlproject.repository.UserDao;
import com.personalsoft.sqlproject.service.UserService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=MySqlProjectApplication.class)
@WebMvcTest({UserController.class, UserService.class})
class MySqlProjectApplicationTests {
	
	private ObjectMapper mapper = new ObjectMapper();
	private static final Logger logger = LoggerFactory.getLogger(MySqlProjectApplicationTests.class);
	
	@Autowired
	UserController userController;

	@Autowired
	MockMvc mock;
	
	@MockBean
	UserDao dao;
	
	UserDto userD;
	Integer userID;
	
	@BeforeEach
	void contextLoads() {
		userD = UserDto.builder().name("Daniel Osorio").email("prueba@test.com").age(18).build();
	}
	
	@Test
	void user_UT01_CreateUserSuccess_ReturnOkAndAnUser() throws Exception {
		logger.info("user_UT01_CreateUserSuccess_ReturnOkAndAnUser()");
		// GIVEN
		//UserDto userD = UserDto.builder().name("Daniel Osorio").email("prueba@test.com").age(15).build();
		userD.setAge(20);
		UserEntity userRequest = UserEntity.builder().name("Daniel Osorio").email("prueba@test.com").build();
		UserEntity userRepositoryResponse = UserEntity.builder().id(1).name("Daniel Osorio").email("prueba@test.com").build();
		
		when(dao.save(any(UserEntity.class))).thenReturn(userRepositoryResponse);
		
		// WHEN
		//UserEntity userResponse = userController.createUser(userD);
		MvcResult mvcRes = getResultPost(userD);
		String userEntityJson = mvcRes.getResponse().getContentAsString();
		UserEntity userEntity = mapper.readValue(userEntityJson, UserEntity.class);
		
		// THEN
		assertEquals("Daniel Osorio", userEntity.getName());
		assertEquals(userEntity.getEmail(), "prueba@test.com");
		assertNotNull(userEntity.getId());
		assertTrue(userD.getAge()>=18);
	}
	
	@Test
	void user_UT02_EditUserSuccess_ReturnOkAndEditedUser() throws Exception {
		logger.info("user_UT02_EditUserSuccess_ReturnOkAndEditedUser()");
		// GIVEN
		//userD.setId(3);
		userID = 3;
		userD.setAge(25);
		UserEntity userRepoResponse = UserEntity.builder().id(3).name("Jose Osorio").email("prueba@test.com").age(25).build();
		Optional<UserEntity> userRepoResponseOptional = Optional.of(userRepoResponse);
		
		UserEntity userRepoResponseEdited = UserEntity.builder().id(3).name("Daniel Osorio").email("prueba@test.com").age(28).build();
		when(dao.findById(any(Integer.class))).thenReturn(userRepoResponseOptional);
		when(dao.save(any(UserEntity.class))).thenReturn(userRepoResponseEdited);
		
		// WHEN
		//UserEntity userResponse = userController.updateUser(userD, userID); 
		MvcResult mvcRes = getResultPut(userD, userID);
		String userEntityJson = mvcRes.getResponse().getContentAsString();
		UserEntity userEntity = mapper.readValue(userEntityJson, UserEntity.class);
		
		// THEN
		assertNotNull(userRepoResponseOptional);
		assertEquals("Daniel Osorio", userEntity.getName());
		assertTrue(userEntity.getAge()>=25);
		
	}
	
	private MvcResult getResultPost(UserDto requestObject) throws Exception {
		String json = mapper.writeValueAsString(requestObject);

		return this.mock.perform(post("/")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andReturn();
	}

	private MvcResult getResultPut(UserDto requestObject, Integer id) throws Exception {
		String json = mapper.writeValueAsString(requestObject);

		return this.mock.perform(put("/".concat(String.valueOf(id)))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andReturn();
	}
}
