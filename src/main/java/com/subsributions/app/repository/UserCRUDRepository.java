package com.subsributions.app.repository;

import com.subsributions.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserCRUDRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"subscriptions"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"subscriptions"})
    Page<User> findAll(Pageable pageable);

    Boolean existsByEmail(String email);
}
