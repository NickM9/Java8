package com.trainigcenter.trainee;

import com.trainigcenter.trainee.service.Java8TaskService;
import com.trainigcenter.trainee.service.MovieService;
import com.trainigcenter.trainee.service.SQlTaskService;
import com.trainigcenter.trainee.service.TaskService;
import java.io.IOException;
import java.nio.file.Paths;



public class Test {

    static MovieService service = new MovieService();
    static TaskService sqlTaskService = new SQlTaskService();
    static TaskService java8TaskService = new Java8TaskService();

    public static void main(String[] args) throws IOException {

        service.downloadMoviesInDb(Paths.get("D:\\MyFiles\\Java Programs\\movies"));
        //service.downloadMoviesInDb(Paths.get("D:\\testFolder"));
        //service.downloadMoviesInDb(Paths.get("movies"));


        // 1. Select all films that last longer than ?(duration); 140
        int minDuration = 140;
        sqlTaskService.getMoviesThatLongerThanDuration(minDuration);
        java8TaskService.getMoviesThatLongerThanDuration(minDuration);

        // 2. Select Top ? shortest films.
        int limit = 5;
        sqlTaskService.getTopShortestMovies(limit);
        java8TaskService.getTopShortestMovies(limit);

        // 3. Select Top 3 longest films for genre ?(name of genre)
        String genre = "Science fiction";
        sqlTaskService.getTop3LongestFilmsForGenre(genre);
        java8TaskService.getTop3LongestFilmsForGenre(genre);

        // 4. Select the number of films in each genre for the period from ? year to ? year
        // and only those ones for which the number of relevant films is more than ?.
        int yearFrom = 2010;
        int yearTo = 2015;
        int count = 3;
        sqlTaskService.getNumberOfFilmsInGenreFromToWithCount(yearFrom, yearTo, count);
        java8TaskService.getNumberOfFilmsInGenreFromToWithCount(yearFrom, yearTo, count);

        // 5. Select the ‘most productive’ actor for every year (with the highest number of films the actor participated).
        sqlTaskService.getTheMostProductiveActorEveryYear();
        java8TaskService.getTheMostProductiveActorEveryYear();

        // 6. Select all actors that have films in genre ? (name of genre).
        genre = "Comedy";
        sqlTaskService.getAllActorsInGenre(genre);
        java8TaskService.getAllActorsInGenre(genre);

//         РАБОТАЕТ
//        Path root = Paths.get("D:\\MyFiles\\Java Programs\\movies");
//        List<Path> jsonPaths = Files.walk(root)
//                .filter(Files::isRegularFile)
//                .filter(path -> path.toString().endsWith(".json"))
//                .collect(Collectors.toList());
//
//        ObjectMapper objectMapper = new ObjectMapper();

//       List<Movie> movies = jsonPaths.stream()
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

//        List<Movie> movies = new ArrayList<>();
//        for (Path path : jsonPaths){
//            File file = path.toFile();
//            System.out.println(file);
//            Movie movie = objectMapper.readValue(file, Movie.class);
//            movies.add(movie);
//        }
//
//       movies.stream().forEach(System.out::println);




    }
}