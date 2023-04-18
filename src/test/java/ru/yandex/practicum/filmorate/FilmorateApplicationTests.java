package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserDbStorage userDB;

    private final FilmDbStorage filmDb;
    User user1, user2;
    Film rambo, nemo;
    ArrayList<Genre> genresNemo;
    ArrayList<Genre> genresRambo;

    @BeforeEach
    public void beforeEach() {
        user1 = User.builder()
                .email("hia99@ya.ru")
                .login("hia99")
                .name("hia99")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();
        user2 = User.builder()
                .email("hope@ya.ru")
                .login("hope")
                .name("hope")
                .birthday(LocalDate.of(1989, 9, 9))
                .build();
        userDB.addFilm(user1);
        userDB.addFilm(user2);
        genresRambo = new ArrayList<Genre>();
        genresRambo.add(Genre.builder().id(1).build());
        genresNemo = new ArrayList<Genre>();
        genresNemo.add(Genre.builder().id(3).name("Мультфильм").build());
        genresNemo.add(Genre.builder().id(2).name("Драма").build());
        rambo = Film.builder().mpa(MPA.NC17).name("Rambo")
                .description("Upon returning to the United States, Vietnam veteran John Rambo has difficulty"
                        + " adjusting to civilian life and wanders the country as a drifter for almost a decade.")
                .duration(Duration.ofMinutes(93)).releaseDate(LocalDate.of(1982, 10, 22))
                .genres(genresRambo)
                .build();
        nemo = Film.builder().mpa(MPA.NC17).name("Finding Nemo")
                .description("Clownfish Marlin lives in an anemone in the Great Barrier Reef")
                .duration(Duration.ofMinutes(100))
                .releaseDate(LocalDate.of(2003, 5, 18))
                .genres(genresNemo)
                .mpa(MPA.G)
                .build();
        filmDb.addFilm(rambo);
        filmDb.addFilm(nemo);
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userDB.getUser(1);
        assertThat(userOptional).isPresent().hasValueSatisfying(user -> {
            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
            assertThat(user).hasFieldOrPropertyWithValue("login", "hia99");
            assertThat(user).hasFieldOrPropertyWithValue("name", "hia99");
            assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.of(1999, 9, 9));
        });
    }

    @Test
    public void getFilmById() {
        Optional<Film> filmOptional = Optional.ofNullable(filmDb.getFilm(2));
        assertThat(filmOptional).isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film).hasFieldOrPropertyWithValue("name", "Finding Nemo");
                    assertThat(film).hasFieldOrPropertyWithValue("description",
                            "Clownfish Marlin lives in an anemone in the Great Barrier Reef");
                    assertThat(film).hasFieldOrPropertyWithValue("duration", Duration.ofMinutes(100));
                    assertThat(film).hasFieldOrPropertyWithValue(
                            "releaseDate", LocalDate.of(2003, 5, 18));
                    assertThat(film).hasFieldOrPropertyWithValue("mpa", MPA.G);


                });
    }

    @Test
    public void updateUser() {
        user2.setId(2);
        user2.setLogin("nohope");
        user2.setEmail("nohope@gmail.net");
        user2.setName("John");
        user2.setBirthday(LocalDate.of(1996, 6, 6));
        Optional<User> userUpdated = Optional.ofNullable(userDB.update(user2));
        assertThat(userUpdated).isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(user).hasFieldOrPropertyWithValue("email", "nohope@gmail.net");
                    assertThat(user).hasFieldOrPropertyWithValue("login", "nohope");
                    assertThat(user).hasFieldOrPropertyWithValue("name", "John");
                    assertThat(user)
                            .hasFieldOrPropertyWithValue(
                                    "birthday", LocalDate.of(1996, 6, 6));
                });
    }

    @Test
    public void updateFilm() {
        Film rambo2 = Film.builder().id(1).name("Rambo2").description("New blood")
                .releaseDate(LocalDate.of(2003, 5, 18))
                .duration(Duration.ofMinutes(111))
                .genres(genresNemo)
                .mpa(MPA.G)
                .build();
        Optional<Film> filmUpdated = Optional.ofNullable(filmDb.update(rambo2));
        assertThat(filmUpdated).isPresent()
                .hasValueSatisfying(film -> {
                    assertThat(film).hasFieldOrPropertyWithValue("name", "Rambo2");
                    assertThat(film).hasFieldOrPropertyWithValue("description", "New blood");
                    assertThat(film).hasFieldOrPropertyWithValue("duration", Duration.ofMinutes(111));
                    assertThat(film).hasFieldOrPropertyWithValue(
                            "releaseDate", LocalDate.of(2003, 5, 18));
                    assertThat(film).hasFieldOrPropertyWithValue("mpa", MPA.G);
                    assertThat(film).hasFieldOrPropertyWithValue(
                            "genres", genresNemo);
                });
    }
}
