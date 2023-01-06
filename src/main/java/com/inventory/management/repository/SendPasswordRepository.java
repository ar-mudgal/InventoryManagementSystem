package com.inventory.management.repository;

import com.inventory.management.entity.SendPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendPasswordRepository extends JpaRepository<SendPassword,Integer> {

}
