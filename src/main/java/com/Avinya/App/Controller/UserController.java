package com.Avinya.App.Controller;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Avinya.App.Model.User;
import com.Avinya.App.Model.User1;
import com.Avinya.App.Repository.User1Repository;
import com.Avinya.App.Repository.UserRepository;
import com.Avinya.App.Security.ErrorUtils;

@RestController
@RequestMapping("/api/customer")
public class UserController {

	
	@Autowired
	User1Repository user1Repository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private JwtConfig jwtConfig;
	
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody User1 user1, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = ErrorUtils.getErrorMessages(bindingResult);
            return ResponseEntity.badRequest().body(errorMessages);
        }
        try {
            validatePassword(user1.getPassword());

            User user = new User();
            user.setEmail(user1.getEmail());
            user.setMobNo(user1.getMobNo());
            user.setPassword(user1.getPassword());
            user.setUserType("user");
            userRepository.save(user);

            User1 savedUser = user1Repository.save(user1);
            return ResponseEntity.ok().body("Welcome!");
        } catch (InvalidPasswordException e) {
            return ResponseEntity.badRequest().body("Password validation failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating customer");
        }
    }

    private void validatePassword(String password) throws InvalidPasswordException {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*()\\-_=+{};:,<.>])(?=.*\\d).{8,}$";
        if (!Pattern.matches(passwordPattern, password)) {
            throw new InvalidPasswordException("Password must contain at least one uppercase letter, one special character, one number, and be at least 8 characters long.");
        }
    }

    // Custom exception class for invalid password
    class InvalidPasswordException extends Exception {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User1> users = user1Repository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving customer");
        }
    }
    
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            Optional<User1> optionalUser1 = user1Repository.findById(id);
            if (optionalUser1.isPresent()) {
                return new ResponseEntity<>(optionalUser1.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching customer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-by-id/{id}")
    public ResponseEntity<?> updateUser1(@PathVariable String id, @RequestBody User1 user1) {
        try {
            if (user1Repository.existsById(id)) {
                user1.setId(id);
                User1 updatedUser1 = user1Repository.save(user1);
                return new ResponseEntity<>(updatedUser1, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating customer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            if (user1Repository.existsById(id)) {
                user1Repository.deleteById(id);
                return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Customer not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting customer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}