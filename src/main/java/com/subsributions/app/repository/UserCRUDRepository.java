package com.subsributions.app.repository;

import com.subsributions.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserCRUDRepository extends JpaRepository<User, Long> {

    @Query(value = """
                   SELECT  *
                   FROM public.users
                   WHERE users.email = :email
                   """, nativeQuery = true)
    Optional<User> getUserByEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);
}
