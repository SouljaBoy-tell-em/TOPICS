package com.project.project.requests.topic_requests.message_requests;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class GetMessagesByTopicIdRequest {
    private int topicId;
}
