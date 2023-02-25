package com.sanket.bloggingplatformcounterservice.repositories;

import com.sanket.bloggingplatformcounterservice.models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}
