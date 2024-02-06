package com.Avinya.App.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Avinya.App.Model.Vendor;

public interface VendorRepository extends MongoRepository<Vendor, String> {

}
