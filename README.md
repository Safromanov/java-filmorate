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
SELECT u.user_id, u.email, u.login, u.user_name, u.birtday  
FROM frienship f  
LEFT JOIN users u ON u.user_id = f.id  
WHERE f.user_id = 1;  




