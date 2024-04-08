package com.project.project.topic_config;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TopicRepository extends CrudRepository<Topic, Long>,
                                          JpaRepository<Topic, Long> {
    @Modifying
    @Query(value = "update topics set topic_name = ?1 where topic_id = ?2",
                                                        nativeQuery = true)
    @Transactional
    void updateByTopicId(String topicName, long topicId);
}
