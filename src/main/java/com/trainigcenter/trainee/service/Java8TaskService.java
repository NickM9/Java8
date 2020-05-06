package com.trainigcenter.trainee.service;

import com.trainigcenter.trainee.entity.Actor;
import com.trainigcenter.trainee.entity.Movie;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Java8TaskService extends TaskService {

    @Override
    public void getMoviesThatLongerThanDuration(int minuteDuration) {
        System.out.println("Select all films that last longer than " + minuteDuration + " Java 8");
        startTime();

        List<Movie> allMovies = moviesDao.findAll();
        List<Movie> moviesLongerThanDuration = allMovies.stream()
                .filter(movie -> movie.getDuration().toMinutes() > minuteDuration)
                .collect(Collectors.toList());
        endMethod(moviesLongerThanDuration);
    }

    @Override
    public void getTopShortestMovies(int limit) {
        System.out.println("Select Top " + limit + " shortest films. Java 8");
        startTime();

        List<Movie> allMovies = moviesDao.findAll();
        List<Movie> moviesTopShortest = allMovies.stream()
                .sorted(Comparator.comparing(Movie::getDuration))
                .limit(limit)
                .collect(Collectors.toList());

        endMethod(moviesTopShortest);
    }

    @Override
    public void getTop3LongestFilmsForGenre(String genre) {
        System.out.println("Select Top 3 longest films for " + genre + " genre. Java8");
        startTime();

        List<Movie> allMovies = moviesDao.findAll();
        List<Movie> top3LongestByGenre = allMovies.stream()
                .filter(movie -> movie.getGenres().contains(genre))
                .sorted((m1, m2) ->m2.getDuration().compareTo(m1.getDuration()))
                .limit(3)
                .collect(Collectors.toList());

        endMethod(top3LongestByGenre);
    }

    @Override
    public void getNumberOfFilmsInGenreFromToWithCount(int yearFrom, int yearTo, int count) {
        System.out.println("Select the number of films in each genre for the period from " +   yearFrom + "  year to " + yearTo + " year");
        System.out.println("and only those ones for which the number of relevant films is more than " + count + ". Java 8");

        startTime();

        List<Movie> allMovies = moviesDao.findAll();

        Map<String, Long> genreToCount = allMovies.stream()
                .filter(movie -> movie.getYear() >= yearFrom && movie.getYear() <= yearTo)
                .map(Movie::getGenres)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        genreToCount.values().removeIf(genreCount -> genreCount <= count);

        endMethod(genreToCount);
    }

    @Override
    public void getTheMostProductiveActorEveryYear() {
        System.out.println("Select the ‘most productive’ actor for every year (with the highest number of films the actor participated). Java 8");
        startTime();
        List<Movie> allMovies = moviesDao.findAll();

    }

    @Override
    public void getAllActorsInGenre(String genre) {
        System.out.println("Select all actors that have films in genre " + genre + ". Java 8");
        startTime();
        List<Movie> allMovies = moviesDao.findAll();

        Set<Actor> actorsInGenre = allMovies.stream()
                .filter(movie -> movie.getGenres().contains(genre))
                .map(Movie::getActors)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        endMethod(actorsInGenre);
    }
}
