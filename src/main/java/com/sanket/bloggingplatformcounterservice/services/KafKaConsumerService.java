package com.sanket.bloggingplatformcounterservice.services;

import com.sanket.bloggingplatformcounterservice.common.AppConstants;
import com.sanket.bloggingplatformcounterservice.common.EventType;
import com.sanket.bloggingplatformcounterservice.common.OperationType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafKaConsumerService {

    private final Logger logger =
            LoggerFactory.getLogger(KafKaConsumerService.class);

    private final BlogService blogService;

    @Autowired
    public KafKaConsumerService(BlogService blogService) {
        this.blogService = blogService;
    }

    @KafkaListener(groupId = AppConstants.GROUP_ID, topics = AppConstants.TOPIC_NAME)
    public void consume(ConsumerRecord<String, Object> consumerRecord) {
        String eventType = consumerRecord.key();
        Map<String, Object> data = (Map<String, Object>) consumerRecord.value();

        switch (EventType.valueOf(eventType)) {
            case BLOG_CREATED -> logger.info("blog created");
            case BLOG_PUBLISHED -> {
                Long blogId = Long.valueOf((Integer) data.get("blogId"));
                blogService.updateTotalPublishedBlogs(blogId, OperationType.INCREASE_BLOG_COUNT);
                logger.info("blog published");
            }
            case BLOG_DELETED -> {
                Long blogId = Long.valueOf((Integer) data.get("blogId"));
                blogService.updateTotalPublishedBlogs(blogId, OperationType.DECREASE_BLOG_COUNT);
                logger.info("blog deleted");
            }
        }
    }
}
