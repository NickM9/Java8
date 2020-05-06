package com.trainigcenter.trainee.dao;

import com.trainigcenter.trainee.entity.Actor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class ActorsDao implements Dao<Actor> {

    private static  final Logger logger = LogManager.getLogger(ActorsDao.class);
    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_ACTOR = "INSERT INTO actors (name) VALUES (?)";
    private static final String COUNT_OF_ACTORS = "SELECT COUNT(*) FROM actors WHERE name = ?";



    //package-private
    ActorsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Actor actor){
        jdbcTemplate.update(INSERT_ACTOR, actor.getName());
        logger.info(actor + "is added in DB");
    }

    public boolean isEntityExist (Actor actor){
        Integer count = jdbcTemplate.queryForObject(COUNT_OF_ACTORS, Integer.class, actor.getName());
        return count > 0;
    }

}


