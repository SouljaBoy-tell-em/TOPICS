# Topics

## РУССКАЯ ДОКУМЕНТАЦИЯ:

### Краткое описание:
Программа способна добавлять топики, в которых содержаться сообщения
от авторизованных пользователей. Авторизация и регистрация происходит,
благодаря генерации jwt-токенов и соединения с базой данных(я использовал mysql, но перенастроил для IMDB: h2. Имя пользователя и пароль в application.properties(sa:password). Чтобы подключиться к ней, нужно: http://localhost:8080/h2-console).

### Тестирование:
Удобно использовать POSTMAN или curl-запросы.

[Скачать PostMan](https://www.postman.com/downloads)

### Что может пользователь:

##### Прежде всего, зарегистрироваться. Для этого используйте следующий запрос:
```sh
POST: http://localhost:8080/auth/register
```
##### с телом запроса(JSON):
```sh
{
    "username":"<your_username>",
    "password":"<your_password>"
}
```

##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/c928d693-362f-4cbb-a7a4-e21da35e89cf)

##### После этого можно авторизоваться:
```sh
POST: http://localhost:8080/auth/login
```
##### с телом запроса(JSON):
```sh
{
    "username":"<your_username>", - то, что вводили выше
    "password":"<your_password>" - то, что вводили выше
}
```

##### В обоих случаях, то есть как после регистрации, так и после авторизации в ответе находится токен доступа:
```sh
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwic3ViIjoidSIsImlhdCI6MTcxMjUzNzYxMSwiZXhwIjoxNzEyNjgxNjExfQ.oajCAEa_TdZiMdAE_4IXsJA5PJ7Snk5UqfjD1pxvaTw
```
##### и его можно использовать для дальнейшей отправки запросов, которые нельзя отправлять не имея токен доступа. По умолчанию зарегистрированный имеет роль обычного пользователя(ROLE_USER).
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/41d08440-7d2d-414b-9880-5a9a708e442a)
##### но если ошибиться в пользовательских данных, то получим 403 ошибку:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/dabd5a07-8cab-4acb-a59a-8c71c28111f7)



##### Создание собственного топика:
```sh
POST: http://localhost:8080/example/topic
```
##### с телом запроса(JSON):
```sh
{
    "topicName":"<topic_name>"
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/27d2e59c-3766-4afb-9eac-f0d0b1e37e04)
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/aaf07251-73db-4b96-b0ff-55b6e9cf2d56)

##### можно увидеть соответствующую запись о том, что топик был успешно создан.

##### Теперь если сделать GET-запрос для получения сообщений:
```sh
GET: http://localhost:8080/example/topic
```
##### то можно увидеть в POSTMAN следующее:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/2ad7985d-fdfd-45e0-a77d-9e84bd1e18c7)

##### Топик был создан с соответствующим сообщением в нем. В базе данных создается таблица из двух колонок: 
    topicId | topicName.

##### Создание сообщения в существующем топике:
```sh
POST: http://localhost:8080/example/message
```
##### с телом запроса(JSON):
```sh
{
    "topicId":<topic_id>,
    "textMessage":"<message>"
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/773e39e2-6f97-4aa1-8ed5-de6e6cca09c2)
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/259561e7-7331-4277-aa22-8e30f67140b2)

##### то есть сообщение попадет в топик с указанным topicId. При этом создастся общая таблица для всех сообщений со столбцами:
    messageId | creationDate | textMessage | topicId | userId
##### Проверим это:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/89485768-125d-4469-a94f-3ae07e2f8740)
##### Как видим теперь в топике 2 сообщения.

##### Чтобы изменить сообщение, нужно быть, прежде всего, авторизованным. Когда вы авторизованы, то можно отправить запрос на изменение сообщения в топике:
```sh
PATCH: http://localhost:8080/example/message
```
##### с телом запроса(JSON):
```sh
{
    "topicId":<topic_id>,
    "textMessage":"<new_message>"
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/79fefca2-a618-4d76-b275-4af2e756d5d8)
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/2e973a0e-54c1-4994-9fba-000d0ba5c08c)
##### Теперь если взглянуть на список сообщений, то увидим: 
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/d3f1c703-9cc8-4617-ab78-d8cd617a475d)
##### что сообщение обновилось.

##### Для удаления отправьте запрос:
```sh
DELETE: http://localhost:8080/example/message
```
##### с телом запроса(JSON):
```sh
{
    "messageId":<message_id>
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/80d1a7e0-0557-4f37-82c6-268e97676752)
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/5995bbba-0483-4af4-a85c-918f08167792)

##### Чтобы получить список топиков(здесь уже не нужно ничего писать в теле запроса, потому что список топиков может быть доступен каждому):
```sh
GET: http://localhost:8080/example/get_topics
```
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/0a541d91-a875-42ae-aecb-3c536f1c9ab0)

### Пагинация для топиков и сообщений:
##### Чтобы распологать определенное количество топиков на странице, я добавил параметры size и page.
    - size - параметр, указывающий, что количество элементов на странице
    - page - номер соответствующей страницы
##### Их можно отправить с соответствующими запросами:

##### Для топиков(тело запроса отсутствует):
```sh
GET http://localhost:8080/example/get_topics?size=<your_size>&page=<your_page>
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/5a6c779c-8025-41ae-8c13-bca3c7e7fe14)


##### Для сообщений(тело запроса - это id топика):
```sh
GET http://localhost:8080/example/topic?size=<your_size>&page=<your_page>
```
##### Тело запроса(JSON):
```sh
{
    "messageId":<message_id>
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/942600bb-bda7-405d-a78a-6144719bb2a9)


### REST-API АДМИНИСТРАТОРА:
##### Администратор может редактировать и удалять топики и сообщения. Для того, чтобы сделать пользователя администратором, я пропишу следующий sql-запрос, но всегда несложно реализовать это в коде:
```sh
update users set user_role = 1 where username = 'username';
```
##### Чтобы изменять/удалять сообщения пользователей нужно использовать те же самые команды, что описаны выше, только нужно выбрать id. Давайте попробуем изменить название топика или вообще удалить его.

##### Для изменения имени топика:
```sh
PATCH http://localhost:8080/example/topic
```
##### с телом запроса(JSON):
```sh
{
    "topicId":<topic_id>,
    "topicName":"<new_topic_name>"
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/863a2308-34d9-4e08-9ec8-d42f83c2f76d)
##### Теперь получим топики и увидим новое имя:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/a532e649-e017-4073-8eda-5817efd61ce7)

##### Для удаления топика:
```sh
DELETE http://localhost:8080/example/topic
```
##### с телом запроса(JSON):
```sh
{
    "topicId":<topic_id>,
}
```
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/737087ed-cc00-4e83-859d-3dbd46a189b3)

##### Теперь получим топики и видим, что первый в списке - это топик под номером 4. Удаление произошло:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/055582aa-3e3d-43b5-b82a-6e1537b51556)



### Итоги:
##### Думаю, это все, что я хотел показать. В данном проекте рассматривались:
    - регистрация и авторизация пользователей используя JWT
    - создание и удаление сообщений в топиках
    - пагинация (т.е получения элементов путем запроса с аттрибутами size и page)
    - введения администратора для модерирования контента
##### Если будут какие-то вопросы по коду - пишите: zaitsev.aleksandr@phystech.edu


## ENGLISH DOCUMENTATION:

### Short description:
The program is able to add topics that contain messages
from authorized users. Authorization and registration takes place
due to the generation of jwt tokens and connection to the database (I used MySQL, but reconfigured for IMDB: h2. Username and password in application.properties).

### Testing:
It is convenient to use POSTMAN or curl queries.

[Download PostMan](https://www.postman.com/downloads)

### What the user can do:

##### First of all, register. To do this, use the following query:
```sh
POST: http://localhost:8080/auth/register
```
##### with the request body(JSON):
```sh
{
    "username":"<your_username>",
    "password":"<your_password>"
}
```

##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/c928d693-362f-4cbb-a7a4-e21da35e89cf)

##### After that, you can log in:
```sh
POST: http://localhost:8080/auth/login
```
##### with the request body(JSON):
```sh
{
    "username":"<your_username>", - what was entered above
    "password":"<your_password>" - what was entered above
}
```

##### In both cases, that is, both after registration and after authorization, the access token is in the response:
```sh
eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwic3ViIjoidSIsImlhdCI6MTcxMjUzNzYxMSwiZXhwIjoxNzEyNjgxNjExfQ.oajCAEa_TdZiMdAE_4IXsJA5PJ7Snk5UqfjD1pxvaTw
```
##### and it can be used to further send requests that cannot be sent without having an access token. By default, the registered user has the role of a regular user (ROLE_USER).
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/41d08440-7d2d-414b-9880-5a9a708e442a)
##### but if you make a mistake in the user data, we get a 403 error:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/dabd5a07-8cab-4acb-a59a-8c71c28111f7)



##### Creating your own topic:
```sh
POST: http://localhost:8080/example/topic
```
##### with the request body(JSON):
```sh
{
    "topicName":"<topic_name>"
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/27d2e59c-3766-4afb-9eac-f0d0b1e37e04)
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/aaf07251-73db-4b96-b0ff-55b6e9cf2d56)

##### you can see the corresponding entry stating that the topic was successfully created.

##### Now if you make a GET request to receive messages:
```sh
GET: http://localhost:8080/example/topic
```
##### then you can see the following in POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/2ad7985d-fdfd-45e0-a77d-9e84bd1e18c7)

##### The topic was created with the corresponding message in it. A table with two columns is created in the database:
topicId | topicName.

##### Creating a message in an existing topic:
```sh
POST: http://localhost:8080/example/message
```
##### with the request body(JSON):
```sh
{
    "topicId":<topic_id>,
    "textMessage":"<message>"
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/773e39e2-6f97-4aa1-8ed5-de6e6cca09c2)
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/259561e7-7331-4277-aa22-8e30f67140b2)

##### that is, the message will end up in the topic with the specified topicId. This creates a common table for all messages with columns:
    messageId | creationDate | textMessage | topicId | userId
##### Let's check it out:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/89485768-125d-4469-a94f-3ae07e2f8740)
##### As we can see now there are 2 messages in the topic.

##### To change a message, you need to be, first of all, authorized. When you are logged in, you can send a request to change the message in the topic:
```sh
PATCH: http://localhost:8080/example/message
```
##### with the request body(JSON):
```sh
{
    "topicId":<topic_id>,
    "textMessage":"<new_message>"
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/79fefca2-a618-4d76-b275-4af2e756d5d8)
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/2e973a0e-54c1-4994-9fba-000d0ba5c08c)
##### Now if we look at the list of messages, we will see: 
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/d3f1c703-9cc8-4617-ab78-d8cd617a475d)
##### that the message has been updated.

##### To delete, send a request:
```sh
DELETE: http://localhost:8080/example/message
```
##### with the request body(JSON):
```sh
{
    "messageId":<message_id>
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/80d1a7e0-0557-4f37-82c6-268e97676752)
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/5995bbba-0483-4af4-a85c-918f08167792)

##### To get a list of topics (here you no longer need to write anything in the request body, because the list of topics can be accessed by everyone):
```sh
GET: http://localhost:8080/example/get_topics
```
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/0a541d91-a875-42ae-aecb-3c536f1c9ab0)

### Pagination for topics and messages:
##### To place a certain number of topics on a page, I added the size and page parameters.
    - size is a parameter indicating that the number of elements on the page
    - page is the number of the corresponding page
##### They can be sent with appropriate requests:

##### For topics (the request body is missing):
```sh
GET http://localhost:8080/example/get_topics?size=<your_size>&page=<your_page>
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/5a6c779c-8025-41ae-8c13-bca3c7e7fe14)


##### For messages (the request body is the topic id):
```sh
GET http://localhost:8080/example/topic?size =<your_size>&page=<your_page>
```
##### Request body(JSON):
```sh
{
    "messageId":<message_id>
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/942600bb-bda7-405d-a78a-6144719bb2a9)


### REST API OF THE ADMINISTRATOR:
##### The administrator can edit and delete topics and messages. In order to make the user an administrator, I will write the following sql query, but it is always easy to implement this in the code:
```sh
update users set user_role = 1 where username = 'username';
```
##### To edit/delete user messages, you need to use the same commands as described above, just select the id. Let's try to change the name of the topic or delete it altogether.

##### To change the topic name:
```sh
PATCH http://localhost:8080/example/topic
```
##### with the request body(JSON):
```sh
{
    "topicId":<topic_id>,
    "topicName":"<new_topic_name>"
}
```
##### POSTMAN:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/863a2308-34d9-4e08-9ec8-d42f83c2f76d)
##### Now we will get the topics and see the new name:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/a532e649-e017-4073-8eda-5817efd61ce7)

##### To delete a topic:
```sh
DELETE http://localhost:8080/example/topic
```
##### with the request body(JSON):
```sh
{
    "topicId":<topic_id>,
}
```
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/737087ed-cc00-4e83-859d-3dbd46a189b3)

##### Now we get the topics and see that the first one in the list is the topic number 4. The deletion occurred:
![image](https://github.com/SouljaBoy-tell-em/JAVA/assets/60592559/055582aa-3e3d-43b5-b82a-6e1537b51556)



### Results:
##### I think that's all I wanted to show. This project considered:
    - registration and authorization of users using JWT
    - creation and deletion of messages in topics
    - pagination (i.e. obtaining elements by requesting with size and page attributes)
    - introduction of an administrator to moderate content
##### If you have any questions about the code, please write: zaitsev.aleksandr@phystech.edu
