package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.dao.impl.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
		@Sql(scripts = {"/schema.sql"},config = @SqlConfig(encoding = "UTF-8")),
		@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "/test-data.sql"),
		@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "/drop-data.sql"),
})
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final UserDbFriendsService userService;
	private final FilmDbStorage filmStorage;
	private final FilmDbService filmService;
	private final GenreDbService genreService;
	private final MpaDbService mpaService;

	private User user(int id, String email, String login, String name, LocalDate birthday) {
		User user = new User();
		user.setId(id);
		user.setEmail(email);
		user.setLogin(login);
		user.setName(name);
		user.setBirthday(birthday);
		return user;
	}

	private Film film(int id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
		Film film = new Film();
		film.setId(id);
		film.setName(name);
		film.setDescription(description);
		film.setReleaseDate(releaseDate);
		film.setDuration(duration);
		film.setMpa(mpa);
		return film;
	}
	@Test
	void contextLoads() {
	}

	//UserDbStorage
	@Test
	void getAllUsers() {
		List<User> users = userStorage.allUsers();
		assertEquals(12, users.size());
	}
	@Test
	void findUserById() {
		Optional<User> userOptional = userStorage.getUserById(1);
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}
	@Test
	void addUser() {
		User testUser = userStorage.add(user(0,"adress@mail.ru","login"
				,"name",LocalDate.of(1976, 12, 31)));
		assertThat(testUser)
				.hasFieldOrPropertyWithValue("email", "adress@mail.ru")
				.hasFieldOrPropertyWithValue("login", "login")
				.hasFieldOrPropertyWithValue("name", "name")
				.hasFieldOrPropertyWithValue("birthday", LocalDate.of(1976, 12, 31));
	}
	@Test
	void updateUser() {
		User testUser = userStorage.update(user(2,"adress2@mail.ru","login2"
				,"name2",LocalDate.of(1976, 12, 31)));

		assertThat(testUser)
				.hasFieldOrPropertyWithValue("email", "adress2@mail.ru")
				.hasFieldOrPropertyWithValue("login", "login2")
				.hasFieldOrPropertyWithValue("name", "name2")
				.hasFieldOrPropertyWithValue("birthday", LocalDate.of(1976, 12, 31));
	}

	@Test
	void getFriends() {
		assertEquals(0, userService.friendsOfUser(1).size());

		userService.addToFriends(1,2);
		assertEquals(1, userService.friendsOfUser(1).size());
		assertEquals(0, userService.commonFriends(1,3).size());

		userService.addToFriends(3,2);
		assertEquals(1, userService.commonFriends(1,3).size());

		userService.deleteFromFriends(1,2);
		assertEquals(0, userService.friendsOfUser(1).size());
	}

	@Test
	void allFilms() {
		List<Film> films = filmStorage.allFilms();
		assertEquals(3, films.size());
	}

	@Test
	void findFilmById() {
		Optional<Film> filmOptional = filmStorage.getFilmById(1);
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	void addFilm() {
		Mpa mpa = Mpa.builder()
				.id(1)
				.name("").build();
		Film testFilm = filmStorage.add(film(0,"Film"
				,"Very Interesting",LocalDate.of(2021, 12, 31),120,mpa));
		assertThat(testFilm)
				.hasFieldOrPropertyWithValue("name", "Film")
				.hasFieldOrPropertyWithValue("description", "Very Interesting")
				.hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2021, 12, 31))
				.hasFieldOrPropertyWithValue("duration",120);
	}

	@Test
	void updateFilm() {
		Mpa mpa = Mpa.builder()
				.id(1)
				.name("").build();
		Film testFilm = filmStorage.update(film(1,"Updated film"
				,"Incredible film",LocalDate.of(2019, 12, 31),121,mpa));
		assertThat(testFilm)
				.hasFieldOrPropertyWithValue("id", 1)
				.hasFieldOrPropertyWithValue("name", "Updated film")
				.hasFieldOrPropertyWithValue("description", "Incredible film")
				.hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(2019, 12, 31))
				.hasFieldOrPropertyWithValue("duration",121);
	}

	@Test
	void likeFunctional() {
		filmService.addLike(4,1);
		assertEquals(2, filmStorage.getFilmById(1).orElse(null).getLikes().size());

		filmService.deleteLike(4,1);
		assertEquals(1, filmStorage.getFilmById(1).orElse(null).getLikes().size());
	}

	@Test
	void getBestFilms() {
		Set<Film> bestFilms = filmService.getBestFilms(2);
		List<Film> filmsList = new ArrayList<>(bestFilms);
		assertEquals(2, bestFilms.size());
		assertEquals(filmStorage.getFilmById(2).orElse(null), filmsList.get(0));
		assertEquals(filmStorage.getFilmById(3).orElse(null), filmsList.get(1));
	}
	@Test
	void testGenres() {
		Set<Genre> allGenres = genreService.allGenres();
		Genre genre = genreService.genreById(2).orElse(null);
		assertEquals(6, allGenres.size());
		assertEquals("Драма", genre.getName());
	}
	@Test
	void testMpa() {
		Set<Mpa> allMpa = mpaService.allMpa();
		Mpa mpa = mpaService.mpaById(2).orElse(null);
		assertEquals(5, allMpa.size());
		assertEquals("PG", mpa.getName());
	}
}