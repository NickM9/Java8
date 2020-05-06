package com.trainigcenter.trainee.service;

import com.trainigcenter.trainee.dao.ActorsDao;
import com.trainigcenter.trainee.dao.DaoFactory;
import com.trainigcenter.trainee.dao.GenresDao;
import com.trainigcenter.trainee.dao.MoviesDao;
import com.trainigcenter.trainee.entity.Actor;
import com.trainigcenter.trainee.entity.Movie;
import com.trainigcenter.trainee.service.util.JsonMovieParser;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class MovieService {
	
	private static DaoFactory daoFactory = DaoFactory.getInstace();
	private static MoviesDao moviesDao = daoFactory.getMoviesDao();
	private static ActorsDao actorsDao = daoFactory.getActorsDao();
	private static GenresDao genresDao = daoFactory.getGenresDao();
	private JsonMovieParser jsonMovieParser = new JsonMovieParser();

	
	public void addMovie(Movie movie) {
		if (!moviesDao.isEntityExist(movie)) {
			saveMovie(movie);
			saveActors(movie.getActors());
			saveGenres(movie.getGenres());
			saveMovieActors(movie);
			saveMovieGenres(movie);
		}
	}

	private void saveMovie(Movie movie){
		moviesDao.save(movie);
	}

	private void saveActors(Set<Actor> actors){
		for (Actor actor : actors){
			if (!actorsDao.isEntityExist(actor)) {
				actorsDao.save(actor);
			}
		}
	}

	private void saveGenres(Set<String> genres){
		for (String genre : genres){
			if (!genresDao.isEntityExist(genre)){
				genresDao.save(genre);
			}
		}
	}
 
	private void saveMovieActors(Movie movie) {
		for (Actor actor : movie.getActors()){
			moviesDao.saveActor(movie, actor);
		}
	}
	
	private void saveMovieGenres(Movie movie) {
		for (String genre : movie.getGenres()){
			moviesDao.saveGenres(movie, genre);
		}
	}

	public void downloadMoviesInDb(Path root){

		List<Movie> movies = jsonMovieParser.getAllMoviesFromJsonPath(root);
		for (Movie movie : movies){
			addMovie(movie);
		}

	}

}
