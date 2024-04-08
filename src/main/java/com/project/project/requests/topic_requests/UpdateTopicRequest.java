package com.project.project.requests.topic_requests;


import lombok.Data;

@Data
public class UpdateTopicRequest {
    private long topicId;
    private String topicName;
}
