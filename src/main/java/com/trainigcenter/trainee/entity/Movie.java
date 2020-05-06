package com.trainigcenter.trainee.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.trainigcenter.trainee.entity.deserializer.JsonDurationDeserializer;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class Movie extends Entity {

    private String title;
    private Set<Actor> actors;
    private int year;
    @JsonDeserialize(using = JsonDurationDeserializer.class)
    private Duration duration;
    private Set<String> genres;

    public Movie(){}

    public Movie(int id, String title, int year, Duration duration) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.duration = duration;
        actors = new HashSet<>();
        genres = new HashSet<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (year != movie.year) return false;
        if (title != null ? !title.equals(movie.title) : movie.title != null) return false;
        if (actors != null ? !actors.equals(movie.actors) : movie.actors != null) return false;
        if (duration != null ? !duration.equals(movie.duration) : movie.duration != null) return false;
        return genres != null ? genres.equals(movie.genres) : movie.genres == null;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (actors != null ? actors.hashCode() : 0);
        result = 31 * result + year;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (genres != null ? genres.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", actors=" + actors +
                ", year=" + year +
                ", duration=" + duration +
                ", genres=" + genres +
                ", id=" + id +
                '}';
    }
}
