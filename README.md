# java-filmorate
##Схема БД
![Схема БД](src/main/resources/filmorate_DB.jpg)

## Примеры запросов
### Запрос списка фильмов жанра Комедия
SELECT film_name  
FROM films f  
LEFT JOIN ganre_film gf on f.film_id = gf.film_id  
LEFT JOIN ganre g on g.ganre_id = gf.film_id  
WHERE ganre_name = "Комедия" 
  
### Запрос списка друзей пользователя 
SELECT name_user
FROM users u1
LEFT JOIN friendship AS f ON f.user_id = u1.user_id  
LEFT JOIN users AS u2 ON f.friend_id = u2.user_id  
WHERE f.is_aprooved = true and user_id = 1



