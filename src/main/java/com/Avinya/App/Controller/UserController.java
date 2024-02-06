package com.Avinya.App.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/user")
public class UserController {

	
	@Autowired
	User1Repository user1Repository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private JwtConfig jwtConfig;
	
	@GetMapping
	public ResponseEntity<?> welcome()
	{
		return ResponseEntity.ok().body("Welcome...!");
	}
	
	@PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User1 user1) {
        try {
        	User user = new User();
        	user.setEmail(user1.getEmail());
        	user.setMobNo(user1.getMobNo());
        	user.setPassword(user1.getPassword());
        	user.setUserType("user");
        	userRepository.save(user);
            User1 savedUser = user1Repository.save(user1);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }

    @GetMapping("getAll")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User1> users = user1Repository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving users");
        }
    }
    
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        try {
            Optional<User1> optionalUser1 = user1Repository.findById(id);
            if (optionalUser1.isPresent()) {
                return new ResponseEntity<>(optionalUser1.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching User: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating User: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            if (user1Repository.existsById(id)) {
                user1Repository.deleteById(id);
                return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting User: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginReq loginReq) {
//        // Validate loginReq (e.g., check if email or mobile number is present)
//
//        // Authenticate the user
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword())
//        );
//
//        // Set authentication in the SecurityContext
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Generate JWT token
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String jwtToken = generateJwtToken(userDetails);
//
//        // Save JWT token in userRepository (you might want to enhance this part based on your data model)
//        Optional<User> opuser = userRepository.findByEmail(loginReq.getEmail());
//        User user = opuser.get();
//        user.setJwt(jwtToken);
//        userRepository.save(user);
//
//        // Return response with user type and JWT token
//        return ResponseEntity.ok(new LoginResponse(user.getUserType(), jwtToken));
//    }
    
   
//    @PostMapping("/token/reset")
//    public ResponseEntity<LoginResponse> resetToken() {
//        // Get the authenticated user's username from SecurityContext
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        // Invalidate old JWT token
//        Optional<User> opUser = userRepository.findByEmail(username);
//        if (opUser.isPresent()) {
//            User user = opUser.get();
//            user.setJwt(null);  // Invalidate the old JWT token
//            userRepository.save(user);
//        }
//
//        // Generate a new JWT token
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String newJwtToken = generateJwtToken(userDetails);
//
//        // Save the new JWT token in userRepository
//        Optional<User> opNewUser = userRepository.findByEmail(username);
//        if (opNewUser.isPresent()) {
//            User newUser = opNewUser.get();
//            newUser.setJwt(newJwtToken);
//            userRepository.save(newUser);
//        }
//
//        return ResponseEntity.ok(new LoginResponse(username, newJwtToken));
//    }
//
//    private String generateJwtToken(UserDetails userDetails) {
//        return Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
//                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret())
//                .compact();
//    }
}

