# java-filmorate
## Схема БД
![Схема БД](src/main/resources/filmorate_DB.jpg)

## Примеры запросов
### Запрос списка фильмов жанра Комедия
SELECT film_name  
FROM films f  
LEFT JOIN genre_film gf on f.film_id = gf.film_id  
LEFT JOIN genre g on g.genre_id = gf.film_id  
WHERE genre_name = "Комедия" 
  



