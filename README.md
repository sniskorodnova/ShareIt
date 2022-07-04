# java-shareit
В данном проекте реализовано приложение ShareIt на Spring Boot.

В приложении есть возможность создавать, редактировать, удалять пользователей, а также получать список всех  
пользователей и пользователя по id.
Также есть возможность создавать, редактировать вещи, которые могут быть выставлены для шеринга,  
а также получать список всех вещей, вещи по id и искать вещи, содержащие в названии и описании определенный текст.

Взаимодействие с приложением происходит по API.  
Методы для работы с пользователями:  
POST /users - создание пользователя  
PATCH /users/{id} - редактирование пользователя  
GET /users - получение списка всех пользователей  
GET /users/{userId} - получение информации о пользователе по его id  
DELETE /users/{userId} - удаление пользователя 

Методы для работы с вещами:  
POST /items - создание вещи  
PATCH /items/{id} - редактирование вещи  
GET /items - получение списка всех вещей  
GET /items/{itemId} - получение информации о вещи по ее id  
GET /items/search?text={text} - поиск всех вещей, у которых в названии или описании есть буквосочетание text

Для сохранения пользователей и вещей используются хэш-таблицы <Long, User> и <Long, Item>.
