package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.film.directorDb.DirectorDb;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final DirectorDb directorDb;

    @Override
    public Director getDirector(long id) {
        return directorDb.getDirector(id);
    }

    @Override
    public Director createDirector(Director director) {
        return directorDb.createDirector(director);
    }

    @Override
    public List<Director> findAllDirectors() {
        return directorDb.findAll();
    }

    @Override
    public Director update(Director director) {
        return directorDb.update(director);
    }

    @Override
    public Void deleteDirector(long id) {
        return directorDb.deleteDirector(id);
    }

}
