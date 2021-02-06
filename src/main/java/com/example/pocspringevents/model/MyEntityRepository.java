package com.example.pocspringevents.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyEntityRepository extends CrudRepository<MyEntity, String> {

}
