package com.Avinya.App.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Avinya.App.Model.Order;

public interface OrderRepository extends MongoRepository<Order,String> {

    List<Order> findAllByUser(String user_id);
}
