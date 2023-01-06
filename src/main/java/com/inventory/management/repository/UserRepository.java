package com.inventory.management.repository;

import com.inventory.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmail(String emial);

    User findByEmailAndPassword(String email, String password);
}
