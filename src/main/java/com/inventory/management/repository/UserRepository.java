package com.inventory.management.repository;

import com.inventory.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

//    @Query(value = "select * from project_db.rating_table r inner join project_db2.user_table u on r.user_id = u.id", nativeQuery = true)
//    List<Object[]> findByUserId();

    Optional<User> findByEmail(String emial);

//    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    List<User> findByMobile(String userName);

    User findByMobileAndPassword(String mobile, String password);

    @Query(nativeQuery = true, value ="Select * from user_table  where user_name like %:user_name%")
    List<User> findByName(@Param("user_name") String name);

}
