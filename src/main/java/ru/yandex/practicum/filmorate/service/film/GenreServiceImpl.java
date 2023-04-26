package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.GenreDb.GenreDB;

import java.util.List;


@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    GenreDB genreDB;

    @Override
    public Genre getGenre(int id) {
        return genreDB.getGenre(id);
    }

    @Override
    public List<Genre> findAllGenres() {
        return genreDB.findAll();
    }

}
