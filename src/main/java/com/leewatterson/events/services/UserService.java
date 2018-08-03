package com.leewatterson.events.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.leewatterson.events.models.User;
import com.leewatterson.events.repositories.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //Register (create) user and hash their password
  	public User registerUser(User user) {
  		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
  		user.setPassword(hashed);
  		return userRepository.save(user);
  	}
  	
  	public User findByEmail(String email) {
  		return userRepository.findByEmail(email);
  	}
  	
  	public User findById(Long id) {
  		Optional<User> optionalUser = userRepository.findById(id);
  		if(optionalUser.isPresent()) {
  			return optionalUser.get();
  		}
  		else {
  			return null;
  		}
  	}
  	
  	//Authenticate user
  	public boolean authenticateUser(String email, String password) {
  		User user = userRepository.findByEmail(email);
  		if(user == null) {
  			return false;
  		}
  		else {
  			//check plain text pw against stored hashed pw
  			if(BCrypt.checkpw(password, user.getPassword())) {
  				return true;
  			}
  			else {
  				return false;
  			}
  		}
  	}
}
