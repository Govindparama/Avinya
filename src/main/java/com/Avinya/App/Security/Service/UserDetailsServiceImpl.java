package com.Avinya.App.Security.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Avinya.App.Model.User;
import com.Avinya.App.Repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Optional<User> userOptional = userRepository.findByEmailOrMobNo(username);
    	User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    	return UserDetailsImpl.build(user);

    }
}