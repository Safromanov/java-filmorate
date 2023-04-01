# java-filmorate
Схема БД
![Схема БД](src/main/resources/filmorate_DB.jpg)

## Примеры запросов
### Запрос 
SELECT film_name  
FROM films f  
LEFT JOIN ganre_film gf on f.film_id = gf.film_id  
LEFT JOIN ganre g on g.ganre_id = gf.film_id  
WHERE ganre_name = "Комедия" 



