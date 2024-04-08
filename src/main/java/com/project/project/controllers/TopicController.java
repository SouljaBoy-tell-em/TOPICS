package com.project.project.controllers;


import com.project.project.requests.topic_requests.CreateTopicRequest;
import com.project.project.requests.topic_requests.DeleteTopicRequest;
import com.project.project.requests.topic_requests.UpdateTopicRequest;
import com.project.project.requests.topic_requests.message_requests.CreateMessageRequest;
import com.project.project.requests.topic_requests.message_requests.DeleteMessageRequest;
import com.project.project.requests.topic_requests.message_requests.GetMessagesByTopicIdRequest;
import com.project.project.requests.topic_requests.message_requests.UpdateMessageRequest;
import com.project.project.topic_config.message_config.Message;
import com.project.project.topic_config.message_config.MessageRepository;
import com.project.project.topic_config.Topic;
import com.project.project.topic_config.TopicRepository;
import com.project.project.user_config.UserRole;
import com.project.project.user_config.UserServiceManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class TopicController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserServiceManager service;

    /**
     * The CREATE_MESSAGE(CreateMessageRequest) creates a new message in the topic.
     * @param request request for message creation.
     * @return status of message creation.
     */
    @PostMapping("/message")
    public String CREATE_MESSAGE(@RequestBody CreateMessageRequest request) {
        if(!topicRepository.findById(request.getTopicId()).isEmpty()) {
            messageRepository.save(new Message(
                    request.getTopicId(),
                    GetAuthUsername(),
                    request.getTextMessage(),
                    LocalDate.now())
            );
            return "message created";
        }
        return "topic with id " + request.getTopicId() + " doesn't exist.";
    }

    /**
     * The CREATE_TOPIC(CreateTopicRequest) creates a new topic.
     * @param request request for topic creation.
     * @return status of topic creation.
     */
    @PostMapping("/topic")
    public String CREATE_TOPIC(@RequestBody CreateTopicRequest request) {
        String topicName = null;
        if((topicName = request.getTopicName()) == null)
            throw new NoSuchElementException();

        Topic currentTopic = new Topic(topicName);
        topicRepository.save(currentTopic);
        messageRepository.save(new Message(currentTopic.getId(),
                GetAuthUsername(),
                "topic created",
                LocalDate.now())
        );
        return "topic with name '" + request.getTopicName() + "' created.";
    }

    /**
     * The DELETE_MESSAGE(DeleteMessageRequest) deletes an existing topic.
     * @param request request for topic removal.
     * @return status of topic removal.
     * @throws NullPointerException message is null
     * @throws NoSuchElementException message doesn't exist.
     */
    @DeleteMapping("/message")
    public String DELETE_MESSAGE(@RequestBody DeleteMessageRequest request) {
        try {
            Message message = messageRepository.findById(request.getMessageId()).get();
            if((message.getUserId().equals(GetAuthUsername()) &&
               messageRepository.amountRowsByTopicId(message.getTopicId()) > 1) ||
               service.GetById(GetAuthUsername()).getRole() == UserRole.ROLE_ADMIN) {

                messageRepository.deleteByMessageId(request.getMessageId());
                return "message with id " + request.getMessageId() + " was deleted.";
            }
        } catch (NullPointerException exception) {
            System.out.println(exception.fillInStackTrace());
        } catch (NoSuchElementException exception) {
            System.out.println(exception.fillInStackTrace());
        }
        return "message wasn't delete.";
    }

    /**
     * The DELETE_TOPIC(DeleteTopicRequest) deletes an existing topic.
     * @param request request for message removal.
     * @return status of message removal.
     */
    @DeleteMapping("/topic")
    @PreAuthorize("hasRole('ADMIN')")
    public String DELETE_TOPIC(@RequestBody DeleteTopicRequest request) {
        if(topicRepository.existsById(request.getTopicId())) {
            topicRepository.deleteById(request.getTopicId());
            messageRepository.deleteByTopicId(request.getTopicId());
            return "topic with id " + request.getTopicId() + " was deleted.";
        }
        return "topic with id " + request.getTopicId() + " doesn't exist.";
    }

    /**
     * The GET_TOPICS(String, String) gets an existing topics.
     * @param paramPage number page.
     * @param paramSize amount of elements on the page.
     * @return elements on the page. If params are null,
     * function returns all topics.
     */
    @GetMapping("/get_topics")
    public List<Topic> GET_TOPICS(@RequestParam(value = "size", required = false) String paramSize,
                                   @RequestParam(value = "page", required = false) String paramPage) {
        List<Topic> allTopics = topicRepository.findAll();
        return PAGINATION(paramSize, paramPage, allTopics);
    }

    /**
     * The GET_MESSAGES(GetMessagesByTopicIdRequest, String, String) gets an existing messages.
     * @param request request to receive messages from a specific topic.
     * @param paramPage number page.
     * @param paramSize amount of elements on the page.
     * @return elements on the page. If params are null,
     * function returns all messages.
     */
    @GetMapping("/topic")
    public List<Message> GET_MESSAGES(@RequestBody GetMessagesByTopicIdRequest request,
                                      @RequestParam(value = "size", required = false) String paramSize,
                                      @RequestParam(value = "page", required = false) String paramPage) {
        List<Message> allMessages = messageRepository.findByTopicId(request.getTopicId());
        return PAGINATION(paramSize, paramPage, allMessages);
    }

    /**
     * The PAGINATION(String, String, List) gets the elements depending on the parameters.
     * @param paramPage number page.
     * @param paramSize amount of elements on the page.
     * @param all all elements of the list.
     * @return elements in the specified range depending on the parameters
     */
    private List PAGINATION(String paramSize, String paramPage, List all) {
        if(paramPage == null || paramSize == null)
            return all;

        int page = Integer.parseInt(paramPage);
        int size = Integer.parseInt(paramSize);
        int amountPages = (int) Math.ceil(((double) all.size()) / Double.parseDouble(paramSize));
        List part = new ArrayList<>();
        int cellIndex = (page == amountPages) ? all.size() : (page * size);

        for(int index = size * (page - 1); index < cellIndex; index++)
            part.add(all.get(index));
        return part;
    }

    /**
     * The UPDATE_MESSAGE(UpdateMessageRequest) updates a certain message.
     * @param request request for message updating.
     * @return status of message updating.
     * @throws NullPointerException message is null
     * @throws NoSuchElementException message doesn't exist.
     */
    @PatchMapping("/message")
    public String UPDATE_MESSAGE(@RequestBody UpdateMessageRequest request) {
        try {
            Message message = messageRepository.findById(request.getMessageId()).get();
            if(message.getUserId().equals(GetAuthUsername()) ||
               service.GetById(GetAuthUsername()).getRole() == UserRole.ROLE_ADMIN) {

                messageRepository.updateByMessageId(request.getTextMessage(), request.getMessageId());
                return "message '" + request.getTextMessage() + "' with id " + request.getMessageId() + " updated.";
            }
        } catch (NullPointerException exception) {
            System.out.println(exception.fillInStackTrace());
        } catch (NoSuchElementException exception) {
            System.out.println(exception.fillInStackTrace());
        }
        return "message wasn't update.";
    }

    /**
     * The UPDATE_TOPIC(UpdateTopicRequest) updates a certain topic.
     * @param request request for updating topic.
     * @return status of topic updating.
     */
    @PatchMapping("/topic")
    @PreAuthorize("hasRole('ADMIN')")
    public String UPDATE_TOPIC(@RequestBody UpdateTopicRequest request) {
        if(topicRepository.existsById(request.getTopicId())) {
            topicRepository.updateByTopicId(request.getTopicName(), request.getTopicId());
            return "topic with id " + request.getTopicId() + " was updated.";
        }
        return "topic with id " + request.getTopicId() + " doesn't exist.";
    }

    /**
     * The GetAuthUsername() checks the status of an authorized user.
     * @return username if authentication was successful.
     * @throws Throwable the user is not logged in.
     */
    public String GetAuthUsername() {
        try {
            return SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName();
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}