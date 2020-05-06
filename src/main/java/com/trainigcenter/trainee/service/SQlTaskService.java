package com.trainigcenter.trainee.service;

import com.trainigcenter.trainee.entity.Actor;
import com.trainigcenter.trainee.entity.Movie;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SQlTaskService extends TaskService {

    @Override
    public void getMoviesThatLongerThanDuration(int minuteDuration) {
        System.out.println("Select all films that last longer than " + minuteDuration + " SQL");
        startTime();

        List<Movie> movies = moviesDao.findMoviesThatLongerThanDuration(minuteDuration);

        endMethod(movies);
    }

    @Override
    public void getTopShortestMovies(int limit) {
        System.out.println("Select Top " + limit + " shortest films. SQL");
        startTime();

        List<Movie> movies = moviesDao.findTopShortestMovies(limit);

        endMethod(movies);
    }

    @Override
    public void getTop3LongestFilmsForGenre(String genre) {
        System.out.println("Select Top 3 longest films for " + genre + " genre. SQL");
        startTime();

        List<Movie> movies = moviesDao.findTop3LongestFilmsForGenre(genre);

        endMethod(movies);
    }

    @Override
    public void getNumberOfFilmsInGenreFromToWithCount(int yearFrom, int yearTo, int count) {
        System.out.println("Select the number of films in each genre for the period from " +   yearFrom + "  year to " + yearTo + " year");
        System.out.println("and only those ones for which the number of relevant films is more than " + count + ". SQL");
        startTime();

        Map<String, Integer> genresCounts = moviesDao.findNumberOfFilmsInGenreFromToWithCountSQL(yearFrom, yearTo, count);

        endMethod(genresCounts);

    }

    @Override
    public void getTheMostProductiveActorEveryYear() {
        System.out.println("Select the ‘most productive’ actor for every year (with the highest number of films the actor participated). SQL");
        startTime();

        Map<Integer, Actor> yearActor = moviesDao.findMostProductiveActorEveryYear();

        endMethod(yearActor);
    }

    @Override
    public void getAllActorsInGenre(String genre) {
        System.out.println("Select all actors that have films in genre " + genre + ". SQL");
        startTime();

        Set<Actor> actors = moviesDao.findAllActorsInGenre(genre);

        endMethod(actors);
    }
}
