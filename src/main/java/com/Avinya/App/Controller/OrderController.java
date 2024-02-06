package com.Avinya.App.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Avinya.App.Model.Order;
import com.Avinya.App.Repository.OrderRepository;
import com.Avinya.App.Repository.UserRepository;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    private final OrderRepository orderRepo;
    private final UserRepository userRepo;

    public OrderController(OrderRepository orderRepo, UserRepository userRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    @PostMapping
    public ResponseEntity<?> addOrderItems(Authentication auth,
                                           @RequestBody Order orderReq){
        String id = userRepo.findByEmail(auth.getName()).get().getId();
        orderReq.setUser(id);
        Order savedOrder =  orderRepo.save(orderReq);
        return ResponseEntity.status(201).body(savedOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> addOrderItems(@PathVariable String id){
        Order order = orderRepo.findById(id).get();
        return ResponseEntity.status(201).body(order);
    }

    @GetMapping("/myorders")
    public ResponseEntity<?> getOrders(Authentication auth){
        String user_id = userRepo.findByEmail(auth.getName()).get().getId();
        List<Order> myorders = orderRepo.findAllByUser(user_id);
        return ResponseEntity.ok(myorders);
    }
}
