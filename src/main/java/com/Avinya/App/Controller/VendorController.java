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
import com.Avinya.App.Model.Vendor;
import com.Avinya.App.Repository.UserRepository;
import com.Avinya.App.Repository.VendorRepository;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/distributor")
public class VendorController {
	
	 @Autowired
	    private VendorRepository vendorRepository;
	 
	 	@Autowired
		UserRepository userRepository;

	 	@PostMapping("/register")
	 	public ResponseEntity<?> createVendor(@RequestBody Vendor vendor) {
	 	    try {
	 	        validatePassword(vendor.getPassword());

	 	        User user = new User();
	 	        user.setEmail(vendor.getEmail());
	 	        user.setMobNo(vendor.getMobileNumber());
	 	        user.setPassword(vendor.getPassword());
	 	        user.setUserType("vendor");
	 	        userRepository.save(user);

	 	        Vendor createdVendor = vendorRepository.save(vendor);
	 	        return new ResponseEntity<>("Distributor created successfully!", HttpStatus.CREATED);
	 	    } catch (InvalidPasswordException e) {
	 	        return new ResponseEntity<>("Password validation failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	 	    } catch (Exception e) {
	 	        return new ResponseEntity<>("Error creating distributor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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


	    @GetMapping("/get-all")
	    public ResponseEntity<?> getAllVendors() {
	        try {
	            List<Vendor> vendors = vendorRepository.findAll();
	            return new ResponseEntity<>(vendors, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>("Error fetching distributors: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @GetMapping("/get-by-id/{id}")
	    public ResponseEntity<?> getVendorById(@PathVariable String id) {
	        try {
	            Optional<Vendor> optionalVendor = vendorRepository.findById(id);
	            if (optionalVendor.isPresent()) {
	                return new ResponseEntity<>(optionalVendor.get(), HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("Distributor not found", HttpStatus.NOT_FOUND);
	            }
	        } catch (Exception e) {
	            return new ResponseEntity<>("Error fetching distributor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @PutMapping("/update-by-id/{id}")
	    public ResponseEntity<?> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
	        try {
	            if (vendorRepository.existsById(id)) {
	                vendor.setId(id);
	                Vendor updatedVendor = vendorRepository.save(vendor);
	                return new ResponseEntity<>(updatedVendor, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("Distributor not found", HttpStatus.NOT_FOUND);
	            }
	        } catch (Exception e) {
	            return new ResponseEntity<>("Error updating distributor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    @DeleteMapping("/delete-by-id/{id}")
	    public ResponseEntity<?> deleteVendor(@PathVariable String id) {
	        try {
	            if (vendorRepository.existsById(id)) {
	                vendorRepository.deleteById(id);
	                return new ResponseEntity<>("Distributor deleted successfully", HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>("Distributor not found", HttpStatus.NOT_FOUND);
	            }
	        } catch (Exception e) {
	            return new ResponseEntity<>("Error deleting distributor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

}
