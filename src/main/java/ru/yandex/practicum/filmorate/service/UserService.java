package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
@Service
public interface UserService {
    Set<User> friendsOfUser(Integer userId);
    Set<User> commonFriends(Integer userId, Integer otherUserId);
}