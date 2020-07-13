package com.personalsoft.sqlproject.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalsoft.sqlproject.model.db.UserEntity;
import com.personalsoft.sqlproject.model.dto.UserDto;
import com.personalsoft.sqlproject.repository.UserDao;


@Service
public class UserService{

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserDao userDao;
	
	public List<UserEntity> list(){
		return (List<UserEntity>)userDao.findAll();
	}
	
	public UserEntity create(UserDto user) {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(user.getEmail());
		userEntity.setName(user.getName());
		return userDao.save(userEntity);
	}
	
	public UserEntity update(UserDto user, Integer id) {
		//get the user, if it doesn't exist put null
		UserEntity userEntity = userDao.findById(id).orElse(null);
		if (userEntity != null && userEntity.getAge() >= 25) {
			userEntity.setName(user.getName());
			userEntity.setAge(user.getAge());
			userDao.save(userEntity);
		} else {
			logger.info("update(): User doesn't exist in bd or his age is less than 25");
		}		
		return userEntity;
	}
}
