package com.personalsoft.sqlproject.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.personalsoft.sqlproject.model.db.UserEntity;

@Repository
public interface UserDao extends CrudRepository<UserEntity, Integer>{
	
}