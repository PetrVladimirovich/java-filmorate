MERGE INTO mpa (id,name)
VALUES (1,'G'),
       (2,'PG'),
       (3,'PG-13'),
       (4,'R'),
       (5,'NC-17');

MERGE INTO genre (id,name)
VALUES (1,'Комедия'),
       (2,'Драма'),
       (3,'Мультфильм'),
       (4,'Триллер'),
       (5,'Документальный'),
       (6,'Боевик');

INSERT INTO film (name, description, release_date, duration, mpa_id)
VALUES ('Internship', 'Film about Google','20130101',119,1),
       ('Hackers', 'Film about hackers','19951212',107,2),
       ('Who Am I', 'Film about German hackers','20140323',102,4),
       ('Snowden', 'Film about Snowden','20160912',135,2),
       ('Mr.Robot', 'Film about networks security','20150624',50,5);

INSERT INTO users (email, login, name, birthday)
VALUES ('user1@gmail.com', 'dog','Doggi','20010111'),
       ('user2@mail.ru', 'cat','catti','20010212'),
       ('user3@yandex.ru', 'fish','fishii','20010313'),
       ('user4@lenta.ru', 'pig','xruxru','20010414'),
       ('user5@rambler.ru', 'human','peter','19910303'),
       ('user6@gmail.com', 'strekoza','strekozii','20010515'),
       ('user7@mail.ru', 'sperrow','sperowi','20010616'),
       ('user8@yandex.ru', 'yahoo','yahyyy','20010717'),
       ('user9@lenta.ru', 'ehyhyhy','xyxyxyxy','20010818');

INSERT INTO film_genre (film_id, genre_id)
VALUES (1,1), (2,4), (3,4), (4,2), (4,5), (5,4);

INSERT INTO film_likes (film_id, liked_from_id)
VALUES ( SELECT id FROM film WHERE name = 'Internship', SELECT id FROM users WHERE name = 'xruxru'),
       ( SELECT id FROM film WHERE name = 'Hackers', SELECT id FROM users WHERE name = 'peter'),
       ( SELECT id FROM film WHERE name = 'Hackers', SELECT id FROM users WHERE name = 'xruxru'),
       ( SELECT id FROM film WHERE name = 'Who Am I', SELECT id FROM users WHERE name = 'catti'),
       ( SELECT id FROM film WHERE name = 'Who Am I', SELECT id FROM users WHERE name = 'peter'),
       ( SELECT id FROM film WHERE name = 'Who Am I', SELECT id FROM users WHERE name = 'yahyyy'),
       ( SELECT id FROM film WHERE name = 'Snowden', SELECT id FROM users WHERE name = 'sperowi'),
       ( SELECT id FROM film WHERE name = 'Mr.Robot', SELECT id FROM users WHERE name = 'sperowi'),
       ( SELECT id FROM film WHERE name = 'Mr.Robot', SELECT id FROM users WHERE name = 'peter'),
       ( SELECT id FROM film WHERE name = 'Mr.Robot', SELECT id FROM users WHERE name = 'yahyyy'),
       ( SELECT id FROM film WHERE name = 'Mr.Robot', SELECT id FROM users WHERE name = 'catti'),
       ( SELECT id FROM film WHERE name = 'Snowden', SELECT id FROM users WHERE name = 'peter');