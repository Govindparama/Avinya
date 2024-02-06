package com.Avinya.App.Controller;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Avinya.App.Model.Product;
import com.Avinya.App.Model.User;
import com.Avinya.App.Repository.ProductRepository;
import com.Avinya.App.Repository.UserRepository;
import com.Avinya.App.dto.CommentReq;
import com.Avinya.App.dto.MessageRes;
import com.Avinya.App.dto.TopProductRes;

@RestController
@RequestMapping("/api/products")
public class ProductController {



    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public ProductController(ProductRepository productRepo, UserRepository userRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }
    
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        try {
            // Save the product to the database
            Product savedProduct = productRepo.save(product);

            return ResponseEntity.status(201).body(savedProduct);
        } catch (Exception e) {
            // Handle any exceptions that may occur during product creation
            return ResponseEntity.status(500).body(new MessageRes("Error adding the product"));
        }
    }


    @GetMapping("/top")
    public ResponseEntity<?> getTopProducts(){

        List<TopProductRes> products;
        try(Stream<TopProductRes> stream = productRepo.getTopProducts()) {
            products = stream.limit(3).collect(Collectors.toList());
        }
        System.out.println(products);
        return ResponseEntity.ok(products);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(@RequestParam(value = "keyword") String keyword,
                                           @RequestParam(value = "pageNumber") int pageNumber){
        String key = keyword.strip();
        Pageable pageable = PageRequest.of(pageNumber - 1, 8);
        if (key.length() == 0){
            Page<Product> products = productRepo.findAll(pageable);
            List<Product> p = products.getContent();
            int page = products.getNumber() + 1;
            int pages = products.getTotalPages();
            Map<String, Object> result = Map.of("page", page, "pages", pages, "products", p);
            return ResponseEntity.ok(result);
        } else {
            Page<Product> products = productRepo.findAllByQ(key, pageable);
            List<Product> p = products.getContent();
            int page = products.getNumber() + 1;
            int pages = products.getTotalPages();
            Map<String, Object> result = Map.of("page", page, "pages", pages, "products", p);
            return ResponseEntity.ok(result);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id){
        Product p = productRepo.findById(id).get();
        List<Product.Review> r =  new ArrayList<>();
        p.setReviews(r);
        return ResponseEntity.ok(p);
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> addComment(Authentication auth, @PathVariable String id, @RequestBody CommentReq req){
        User user = userRepo.findByEmail(auth.getName()).get();
        Optional<Product> p = productRepo.findById(id);
        if (p.isPresent()){
            Product product = p.get();
            var alreadyReviewed = product.getReviews().stream().filter(x -> x.getUser().equals(user.getId())).count();
            if (alreadyReviewed != 0){
                return ResponseEntity.status(400).body(new MessageRes("Product already reviewed"));
            }
            Product.Review r = new Product.Review();
            r.setRating(req.getRating());
            r.setComment(req.getComment());
//            r.setName(user.getName());
            r.setUser(user.getId());
            product.getReviews().add(r);
            int numReviews = product.getNumReviews() + 1;
            Double rating = (product.getRating() + req.getRating())/numReviews;
            product.setNumReviews(numReviews);
            DecimalFormat df = new DecimalFormat("#.0");
            product.setRating(Double.valueOf(df.format(rating)));
            productRepo.save(product);
            return ResponseEntity.status(201).body(new MessageRes("Review added"));

        }
        return ResponseEntity.ok(p);
    }
}
