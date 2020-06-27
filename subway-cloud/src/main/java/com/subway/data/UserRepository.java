package com.subway.data;

import org.springframework.data.repository.CrudRepository;

import com.subway.User;

public interface UserRepository extends CrudRepository<User, Long>{
	User findByUsername(String username);
}
