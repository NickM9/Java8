package com.trainigcenter.trainee.dao;

import com.trainigcenter.trainee.entity.Actor;

import com.trainigcenter.trainee.entity.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Duration;
import java.util.*;

public class MoviesDao implements Dao<Movie> {

    private static  final Logger logger = LogManager.getLogger();
    private JdbcTemplate jdbcTemplate;
    
    private static final String INSERT_MOVIE = "INSERT INTO movies (title, year, duration) VALUES (?, ?, ?)";
	private static final String COUNT_OF_MOVIES = "SELECT COUNT(*) FROM movies WHERE title = ? AND year = ?";
	private static final String INSERT_MOVIES_ACTOR = "INSERT INTO movies_actors VALUES (?, (SELECT id FROM actors WHERE name = ?))";
	private static final String INSERT_MOVIES_GENRE = "INSERT INTO movies_genres VALUES (?, (SELECT id FROM genres WHERE genre = ?))";

	private static final String SELECT_ALL_FULL_MOVIES = "SELECT movies.id, movies.title, movies.year, movies.duration, actors.id AS actor_id, actors.name AS actor_name, genres.genre\n" +
														"FROM movies\n" +
														"JOIN movies_actors ON movies.id = movies_actors.movie_id\n" +
														"JOIN actors ON movies_actors.actor_id = actors.id\n" +
														"JOIN movies_genres ON movies.id = movies_genres.movie_id\n" +
														"JOIN genres ON movies_genres.genre_id = genres.id;";

	private static final String SELECT_ALL_LONGER_THAN_DURATION = "SELECT movies.id, movies.title, movies.year, movies.duration, actors.id AS actor_id, actors.name AS actor_name, genres.genre\n" +
																"FROM movies\n" +
																"JOIN movies_actors ON movies.id = movies_actors.movie_id\n" +
																"JOIN actors ON movies_actors.actor_id = actors.id\n" +
																"JOIN movies_genres ON movies.id = movies_genres.movie_id\n" +
																"JOIN genres ON movies_genres.genre_id = genres.id\n" +
																"WHERE duration > ?";

	private static final String SELECT_TOP_SHORTEST = "SELECT * FROM movies ORDER BY duration LIMIT ?";
	private static final String SELECT_TOP_3_LONGEST_BY_GENRE = "SELECT movies.id, movies.title, movies.year, movies.duration, genres.genre\n" +
																"FROM movies\n" +
																"JOIN movies_genres ON movies.id = movies_genres.movie_id\n" +
																"JOIN genres ON movies_genres.genre_id = genres.id\n" +
																"WHERE genre = ?\n" +
																"ORDER BY duration DESC\n" +
																"LIMIT 3";

	private static final String SELECT_GENRES_BY_BETWEEN_MOVIES_YEAR_AND_COUNT =
			"SELECT genre, count(*)\n" +
					"FROM genres\n" +
					"JOIN movies_genres ON genres.id = movies_genres.genre_id\n" +
					"JOIN movies ON movies_genres.movie_id = movies.id\n" +
					"WHERE movies.year BETWEEN ? AND ?\n" +
					"GROUP BY genre_id\n" +
					"HAVING COUNT(*) > ?";

	private static final String SELECT_MOST_PRODUCTIVE_ACTOR_EVERY_YEAR =
			"SELECT year, id, name, max(count_films)\n" +
					"FROM(SELECT movies.year, actors.id, actors.name, count(name) as count_films\n" +
					"FROM movies\n" +
					"JOIN movies_actors ON movies.id = movies_actors.movie_id\n" +
					"JOIN actors ON movies_actors.actor_id = actors.id\n" +
					"group by name, year\n" +
					"order by year) AS first_table\n" +
					"group by year;";

	private static final String SELECT_ALL_ACTORS_IN_GENRE =
			"SELECT actors.id, actors.name\n" +
					"FROM movies\n" +
					"JOIN movies_genres ON movies.id = movies_genres.movie_id\n" +
					"JOIN genres ON movies_genres.genre_id = genres.id\n" +
					"JOIN movies_actors ON movies.id = movies_actors.movie_id\n" +
					"JOIN actors ON movies_actors.actor_id = actors.id\n" +
					"WHERE genre = ?\n" +
					"GROUP BY name;";
    
    //package-private
    MoviesDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void save(Movie movie) {

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(INSERT_MOVIE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, movie.getTitle());
			ps.setInt(2, movie.getYear());
			ps.setLong(3, movie.getDuration().toMinutes());
			return ps;
		}, keyHolder);

		int key = keyHolder.getKey().intValue();
		movie.setId(key);
		logger.info(movie + "is added in DB");
	}
	
	public boolean isEntityExist(Movie movie) {
    	int count = jdbcTemplate.queryForObject(COUNT_OF_MOVIES, Integer.class, movie.getTitle(), movie.getYear());
    	return count > 0;
	}
 
	public void saveActor(Movie movie, Actor actor) {
		jdbcTemplate.update(INSERT_MOVIES_ACTOR, movie.getId(), actor.getName());
		logger.info(actor + " is added in DB on movie " + movie);
	}
	
	public void saveGenres(Movie movie, String genre) {
		jdbcTemplate.update(INSERT_MOVIES_GENRE, movie.getId(), genre);
		logger.info(genre + " is added in DB on movie " + movie);
	}
	
	public List<Movie> findAll() {

		List<Movie> fullMovies = jdbcTemplate.query(SELECT_ALL_FULL_MOVIES, (ResultSetExtractor<List<Movie>>) rs -> {

			HashMap<Integer, Movie> movies = new HashMap<>();

			while (rs.next()){
				int movieId = rs.getInt("id");
				String title = rs.getString("title");
				int year = rs.getInt("year");
				Duration duration = Duration.ofMinutes(rs.getLong("duration"));
				int actorId = rs.getInt("actor_id");
				String actorName = rs.getString("actor_name");
				String genre = rs.getString("genre");

				Movie movie = movies.get(movieId);
				if (movie == null){
					movie = new Movie(movieId, title, year, duration);
					movies.put(movieId, movie);
				}

				Actor actor = new Actor(actorId, actorName);
				movie.getActors().add(actor);
				movie.getGenres().add(genre);

			}
			return new ArrayList<Movie>(movies.values());
		});

		return fullMovies;
	}

	public List<Movie> findMoviesThatLongerThanDuration(int minuteDuration){

		List<Movie> fullMovies = jdbcTemplate.query(SELECT_ALL_LONGER_THAN_DURATION, new Object[]{minuteDuration},
													(ResultSetExtractor<List<Movie>>) rs -> {

			HashMap<Integer, Movie> movies = new HashMap<>();

			while (rs.next()){
				int movieId = rs.getInt("id");
				String title = rs.getString("title");
				int year = rs.getInt("year");
				Duration duration = Duration.ofMinutes(rs.getLong("duration"));
				int actorId = rs.getInt("actor_id");
				String actorName = rs.getString("actor_name");
				String genre = rs.getString("genre");

				Movie movie = movies.get(movieId);
				if (movie == null){
					movie = new Movie(movieId, title, year, duration);
					movies.put(movieId, movie);
				}

				Actor actor = new Actor(actorId, actorName);
				movie.getActors().add(actor);
				movie.getGenres().add(genre);

			}
			return new ArrayList<Movie>(movies.values());
		});

		return fullMovies;

	}

	public List<Movie> findTopShortestMovies(int limit){
    	List<Movie> movies = jdbcTemplate.query(SELECT_TOP_SHORTEST, new Object[]{limit}, (rs, rowNum) -> {

    		Movie movie = new Movie();

    		movie.setId(rs.getInt("id"));
    		movie.setTitle(rs.getString("title"));
    		movie.setYear(rs.getInt("year"));
    		movie.setDuration(Duration.ofMinutes(rs.getInt("duration")));

    		return movie;
		});
    	return movies;
	}

	public List<Movie> findTop3LongestFilmsForGenre(String genre){
		List<Movie> movies = jdbcTemplate.query(SELECT_TOP_3_LONGEST_BY_GENRE, new Object[]{genre}, (rs, rowNum) -> {

			Movie movie = new Movie();

			movie.setId(rs.getInt("id"));
			movie.setTitle(rs.getString("title"));
			movie.setYear(rs.getInt("year"));
			movie.setDuration(Duration.ofMinutes(rs.getInt("duration")));

			return movie;
		});
		return movies;
	}

	public Map<String, Integer> findNumberOfFilmsInGenreFromToWithCountSQL(int yearFrom, int yearTo, int count){

		Map<String, Integer> genreIntegerMap = jdbcTemplate.query(SELECT_GENRES_BY_BETWEEN_MOVIES_YEAR_AND_COUNT,
				new Object[]{yearFrom, yearTo, count},
				(ResultSetExtractor<Map>) rs -> {
					Map<String, Integer> genresCount = new HashMap<String, Integer>();
					while(rs.next()){
						genresCount.put(rs.getString("genre"), rs.getInt("count(*)"));
					}
					return genresCount;
				});

		return genreIntegerMap;
	}

	public Map<Integer, Actor> findMostProductiveActorEveryYear(){
		Map<Integer, Actor> yearActor = jdbcTemplate.query(SELECT_MOST_PRODUCTIVE_ACTOR_EVERY_YEAR,
				(ResultSetExtractor<Map>) rs -> {
					Map<Integer, Actor> yearActorLocalMap = new HashMap<>();
					while(rs.next()){
						Actor actor = new Actor();
						actor.setId(rs.getInt("id"));
						actor.setName(rs.getString("name"));
						yearActorLocalMap.put(rs.getInt("year"), actor);
					}
					return yearActorLocalMap;
				});

		return yearActor;
	}

	public Set<Actor> findAllActorsInGenre(String genre){
		Set<Actor> actors = new HashSet<>(jdbcTemplate.query(SELECT_ALL_ACTORS_IN_GENRE,
															new BeanPropertyRowMapper<>(Actor.class),
															genre));
		return actors;
	}
}
