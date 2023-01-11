package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
@Service
public interface UserFriendsService {
    void addToFriends(Integer userId, Integer friendId);

    void deleteFromFriends(Integer userId, Integer friendId);

    Set<User> friendsOfUser(Integer userId);

    Set<User> commonFriends(Integer userId, Integer otherUserId);
}
