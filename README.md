# java-filmorate
Template repository for Filmorate project.

# ER-diagram filmorate project

![This is an image](https://github.com/PetrVladimirovich/java-filmorate/blob/b5861119769f538cee28d3b09eaa52aca70cb8e8/ER%20diagram%20filmorate.png)

# Примеры SQL - запросов

**Получение всех пользователей :**
```
SELECT *       
FROM user;
```

**Получение пользователя по userId :**
```
SELECT *       
FROM user 
WHERE id = userId;
```
**Получение списка друзей пользователя по userId :**
```
SELECT u.id AS id_friend,
       u.name,
       u.email
FROM user AS u
WHERE id = (SELECT friends_with
            FROM user_friends
            WHERE id = userId
                  AND confirmed_friend IS TRUE 
           );
```
**Получение всех фильмов :**
```
SELECT *       
FROM film;
```
**Получение фильма по filmId :**
```
SELECT *       
FROM film
WHERE id = filmId;
```

