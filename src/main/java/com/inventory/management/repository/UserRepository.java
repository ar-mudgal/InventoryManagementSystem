package com.inventory.management.repository;

import com.inventory.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

//    @Query(value = "select * from project_db.rating_table r inner join project_db2.user_table u on r.user_id = u.id", nativeQuery = true)
//    List<Object[]> findByUserId();

    User findByEmail(String emial);

    User findByEmailAndPassword(String email, String password);
}
