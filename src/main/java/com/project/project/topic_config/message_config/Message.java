package com.project.project.topic_config.message_config;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "MessageConfig")
@NoArgsConstructor
public class Message {

    @Id
    @Column(name = "MessageId")
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "TopicId")
    @Getter
    private long topicId;

    @Column(name = "UserId")
    @Getter
    @Setter
    private String userId;

    @Column(name = "TextMessage")
    @Getter
    @Setter
    private String textMessage;

    @Column(name = "CreationDate")
    @Getter
    @Setter
    private LocalDate creationDate;

    public Message(long topicId, String userId, String textMessage,
                   LocalDate creationDate) {
        this.topicId = topicId;
        this.userId = userId;
        this.textMessage = textMessage;
        this.creationDate = creationDate;
    }
}