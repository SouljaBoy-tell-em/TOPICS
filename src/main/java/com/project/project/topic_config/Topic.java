package com.project.project.topic_config;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Table(name = "Topics")
public class Topic {

    @Id
    @Column(name = "TopicId")
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "TopicName")
    @Getter
    @Setter
    private String name;

    public Topic(String name) {
        this.name = name;
    }
}
