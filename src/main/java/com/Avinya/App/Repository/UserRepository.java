package com.Avinya.App.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Avinya.App.Model.User;


public interface UserRepository extends MongoRepository<User , String> {

	Optional<User> findByEmail(String email);
	Object findByPassword(String password);
	User findByResetOtpAndEmail(String otp, String email);
//	User findByEmailOrMobNo(String email, String mobNo);
	Optional<User> findByEmailOrMobNo(String username);
}
