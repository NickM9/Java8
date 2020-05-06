CREATE DATABASE IF NOT EXISTS movie_db DEFAULT CHARACTER SET utf8;
USE movie_db;

-- create movies table
CREATE TABLE IF NOT EXISTS movies (
	id INT NOT NULL AUTO_INCREMENT,
	title VARCHAR(70) NOT NULL,
	year INT NOT NULL,
	duration VARCHAR(15) NOT NULL,
	PRIMARY KEY (`id`)
);

-- create actors table
CREATE TABLE IF NOT EXISTS actors (
	id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(45) NOT NULL UNIQUE,
	PRIMARY KEY (id)
);

-- create genres table
CREATE TABLE IF NOT EXISTS genres (
	id INT NOT NULL AUTO_INCREMENT,
	genre VARCHAR(45) NOT NULL UNIQUE,
	PRIMARY KEY (id)
);

-- create actors in movies table
CREATE TABLE IF NOT EXISTS movies_actors (
	movie_id INT NOT NULL,
	actor_id INT NOT NULL,
	PRIMARY KEY (movie_id, actor_id),
	FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (actor_id) REFERENCES actors(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- create genres in movies table
CREATE TABLE IF NOT EXISTS movies_genres (
	movie_id INT NOT NULL,
	genre_id INT NOT NULL,
	PRIMARY KEY (movie_id, genre_id),
	FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE ON UPDATE CASCADE
);