package com.sanket.bloggingplatformcounterservice.services;

import com.sanket.bloggingplatformcounterservice.common.OperationType;
import com.sanket.bloggingplatformcounterservice.models.Blog;
import com.sanket.bloggingplatformcounterservice.models.User;
import com.sanket.bloggingplatformcounterservice.repositories.BlogRepository;
import com.sanket.bloggingplatformcounterservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final TimeService timeService;

    @Autowired
    public BlogService(BlogRepository blogRepository, UserRepository userRepository, TimeService timeService) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.timeService = timeService;
    }

    public void updateTotalPublishedBlogs(Long blogId, OperationType operationType) {
        Long now = timeService.getEpochTime();
        Optional<Blog> dbBlog = blogRepository.findById(blogId);
        if(dbBlog.isPresent()) {
            Blog blog = dbBlog.get();
            List<User> authors = blog.getAuthors();
            authors.forEach(author -> {
                int currBlogCount = author.getTotalBlogs();
                int updatedBlogCount = currBlogCount + switch (operationType) {
                    case INCREASE_BLOG_COUNT -> 1;
                    case DECREASE_BLOG_COUNT -> -1;
                };
                author.setTotalBlogs(updatedBlogCount);
                author.setLastModifiedAt(now);
            });
            blog.setLastModifiedAt(now);
            userRepository.saveAllAndFlush(authors);
            blogRepository.saveAndFlush(blog);
        }
    }
}
