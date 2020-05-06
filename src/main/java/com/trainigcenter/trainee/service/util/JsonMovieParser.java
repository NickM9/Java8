package com.trainigcenter.trainee.service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trainigcenter.trainee.entity.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class JsonMovieParser {

    private static  final Logger logger = LogManager.getLogger(JsonMovieParser.class);

    public List<Movie> getAllMoviesFromJsonPath(Path root){
        List<Path> jsonPaths = getAllJsonPath(root);
        List<Movie> movies = new ArrayList<>();
        try {
            movies = getMoviesFormJsonPath(jsonPaths);
        } catch (IOException e) {
            logger.warn(e);
        }
        return movies;
    }

    private List<Path> getAllJsonPath(Path root){

        List<Path> jsonPaths = new ArrayList<>();

        try {
            jsonPaths = Files.walk(root)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.warn(e);
        }

        return jsonPaths;
    }

    private List<Movie> getMoviesFormJsonPath(List<Path> jsonPaths) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        List<Movie> movies = new ArrayList<>();
        for (Path path : jsonPaths){
            File file = path.toFile();
            Movie movie = objectMapper.readValue(file, Movie.class);
            movies.add(movie);
            file.delete();
        }

        return movies;

        // Спросить как вариант
//        List<Movie> movies = jsonPaths.stream()
//                .map(Path::toFile)
//                .map(file -> {
//                    try {
//                        return objectMapper.readValue(file, Movie.class);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                })
//                .collect(Collectors.toList());




    }



}
