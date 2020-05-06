package com.trainigcenter.trainee.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class GenresDao implements Dao<String> {

    private static  final Logger logger = LogManager.getLogger(GenresDao.class);
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_GENRE = "INSERT INTO genres (genre) VALUES (?)";
    private static final String COUNT_OF_GENRES = "SELECT COUNT(*) FROM genres WHERE genre = ?";



    //package-private
    GenresDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String genre){
        jdbcTemplate.update(INSERT_GENRE, genre);
        logger.info(genre + " is added in DB");
    }

    public boolean isEntityExist (String genre){
        Integer count = jdbcTemplate.queryForObject(COUNT_OF_GENRES, new Object[]{genre}, Integer.class);
        return count > 0;
    }

}
