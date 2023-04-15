# java-filmorate
## Схема БД
![Схема БД](src/main/resources/filmorate_DB.jpg)

## Примеры запросов
### Запрос списка фильмов жанра Комедия
SELECT f.film_name  
FROM films f  
LEFT JOIN genre_film gf on f.film_id = gf.film_id  
LEFT JOIN genre g on g.genre_id = gf.genre_id  
WHERE genre_name = "Комедия";  
  
### Запрос списка друзей пользователя 
SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.USER_NAME, U.BIRTHDAY 
FROM FRIENDSHIP F  
LEFT JOIN USERS U ON U.USER_ID = F.Friend_ID  
WHERE f.USER_ID = 1;  



