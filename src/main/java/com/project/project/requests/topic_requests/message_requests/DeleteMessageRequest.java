package com.project.project.requests.topic_requests.message_requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class DeleteMessageRequest {
    private long messageId;
}
