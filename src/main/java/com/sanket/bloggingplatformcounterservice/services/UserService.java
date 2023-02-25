package com.sanket.bloggingplatformcounterservice.services;

import com.sanket.bloggingplatformcounterservice.common.OperationType;
import com.sanket.bloggingplatformcounterservice.models.User;
import com.sanket.bloggingplatformcounterservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateTotalBlogs(List<Integer> authorIds, OperationType operationType) {
        // convert int list to long type
        List<Long> authorIdsInLong = authorIds.stream().mapToLong(Integer::longValue).boxed().collect(Collectors.toList());
        // find all authors and update the total blogs count
        List<User> authors = userRepository.findAllById(authorIdsInLong);
        authors.forEach(author -> {
            int currBlogCount = author.getTotalBlogs();
            int updatedBlogCount = currBlogCount + switch (operationType) {
                case INCREASE_BLOG_COUNT -> 1;
                case DECREASE_BLOG_COUNT -> -1;
            };
            author.setTotalBlogs(updatedBlogCount);
        });
        userRepository.saveAllAndFlush(authors);
    }

}
