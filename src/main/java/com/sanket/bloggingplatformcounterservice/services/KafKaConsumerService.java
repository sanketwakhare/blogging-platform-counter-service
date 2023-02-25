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

import java.util.List;
import java.util.Map;

@Service
public class KafKaConsumerService {

    private final Logger logger =
            LoggerFactory.getLogger(KafKaConsumerService.class);

    private final UserService userService;

    @Autowired
    public KafKaConsumerService(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(groupId = AppConstants.GROUP_ID, topics = AppConstants.TOPIC_NAME)
    public void consume(ConsumerRecord<String, Object> consumerRecord) {
        String eventType = consumerRecord.key();
        Map<String, Object> data = (Map<String, Object>) consumerRecord.value();
        if (EventType.BLOG_CREATED.toString().equalsIgnoreCase(eventType)) {
            logger.info("blog created");
        } else if (EventType.BLOG_PUBLISHED.toString().equalsIgnoreCase(eventType)) {
            List<Integer> authorIds = (List<Integer>) data.get("authorIds");
            userService.updateTotalBlogs(authorIds, OperationType.INCREASE_BLOG_COUNT);
            logger.info("blog published");
        } else if (EventType.BLOG_DELETED.toString().equalsIgnoreCase(eventType)) {
            List<Integer> authorIds = (List<Integer>) data.get("authorIds");
            userService.updateTotalBlogs(authorIds, OperationType.DECREASE_BLOG_COUNT);
            logger.info("blog deleted");
        }
    }
}
