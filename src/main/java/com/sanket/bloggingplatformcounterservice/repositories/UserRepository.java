package com.sanket.bloggingplatformcounterservice.repositories;

import com.sanket.bloggingplatformcounterservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
