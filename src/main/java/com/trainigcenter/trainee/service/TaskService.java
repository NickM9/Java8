package com.trainigcenter.trainee.service;

import com.trainigcenter.trainee.dao.ActorsDao;
import com.trainigcenter.trainee.dao.DaoFactory;
import com.trainigcenter.trainee.dao.GenresDao;
import com.trainigcenter.trainee.dao.MoviesDao;
import com.trainigcenter.trainee.entity.Actor;
import com.trainigcenter.trainee.entity.Entity;
import com.trainigcenter.trainee.entity.Movie;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;


public abstract class TaskService {

    public static DaoFactory daoFactory = DaoFactory.getInstace();
    protected static MoviesDao moviesDao = daoFactory.getMoviesDao();
    protected static ActorsDao actorsDao = daoFactory.getActorsDao();
    protected static GenresDao genresDao = daoFactory.getGenresDao();

    private Instant start;
    private Instant end;

    public abstract void getMoviesThatLongerThanDuration (int minuteDuration);
    public abstract void getTopShortestMovies(int limit);
    public abstract void getTop3LongestFilmsForGenre(String  genre);
    public abstract void getNumberOfFilmsInGenreFromToWithCount(int yearFrom, int yearTo, int count);
    public abstract void getTheMostProductiveActorEveryYear();
    public abstract void getAllActorsInGenre(String genre);


    public void startTime() {
        start = Instant.now();
    }

    public void endTime(){end = Instant.now();}

    public void printDuration(){
        System.out.println(Duration.between(start, end) + "\n");
    }

    public void endMethod(Collection<? extends Entity> entities){
        endTime();
        printEntityId(entities);
        printDuration();
    }

    public void endMethod(Map map){
        endTime();
        printMap(map);
        printDuration();
    }


    private void printMap(Map map){

        for (Object name: map.keySet()){
            String key = name.toString();
            String value = map.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

    private void printEntityId(Collection<? extends Entity> entities){
        for (Entity entity : entities){
            System.out.print(entity.getId() + " | ");
        }
        System.out.println();
    }

    private void printMovie (Movie movie){
        System.out.println(movie.getId() + movie.getTitle() + " | " + movie.getYear() + " | " + movie.getDuration());
        for (Actor actor : movie.getActors()){
            System.out.println(actor);
        }
        System.out.println(movie.getGenres());
        System.out.println("##############################################\n");
    }

}
