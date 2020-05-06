package com.trainigcenter.trainee.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DaoFactory {
	
	private static DriverManagerDataSource dataSource;
	private static JdbcTemplate jdbcTemplate;
	
	private static final DaoFactory instance = new DaoFactory();
	private static final MoviesDao moviesDao = new MoviesDao(jdbcTemplate);
	private static final ActorsDao actorsDao = new ActorsDao(jdbcTemplate);
	private static final GenresDao genresDao = new GenresDao(jdbcTemplate);
	
	private DaoFactory() {
		dataSource = new DriverManagerDataSource();
    	dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    	dataSource.setUrl("jdbc:mysql://localhost:3306/movie_db?serverTimezone=UTC&useSSL=false");
    	dataSource.setUsername("root");
    	dataSource.setPassword("admin");
    	jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public static DaoFactory getInstace() {
		return instance;
	}
	
	public MoviesDao getMoviesDao() {
		return moviesDao;
	}

	public ActorsDao getActorsDao(){return actorsDao;}

	public GenresDao getGenresDao(){return genresDao;}
}
