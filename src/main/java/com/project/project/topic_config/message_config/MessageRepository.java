package com.project.project.topic_config.message_config;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface MessageRepository extends CrudRepository<Message, Long>,
                                            JpaRepository<Message, Long> {
    @Query(value = "select count(topic_id) from message_config where topic_id = ?1",
                                                                 nativeQuery = true)
    long amountRowsByTopicId(long topicId);

    @Modifying
    @Query(value = "delete from message_config where message_id = ?1",
            nativeQuery = true)
    @Transactional
    void deleteByMessageId(long messageId);

    @Modifying
    @Query(value = "delete from message_config where topic_id = ?1",
                                                 nativeQuery = true)
    @Transactional
    void deleteByTopicId(long topicId);

    @Query(value = "select * from message_config where topic_id = ?1",
                                                   nativeQuery = true)
    List<Message> findByTopicId(long topicId);

    @Modifying
    @Query(value = "update message_config set text_message = ?1 where message_id = ?2",
                                                                    nativeQuery = true)
    @Transactional
    void updateByMessageId(String textMessage, long messageId);
}
