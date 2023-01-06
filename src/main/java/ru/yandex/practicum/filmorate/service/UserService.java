package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private static Integer id = 1;

    public void addToFriends(Integer userId, Integer friendId) {
        if (getUserById(userId) != null && getUserById(friendId) != null) {
            getUserById(userId).addFriend(friendId);
            getUserById(friendId).addFriend(userId);
        } else {
            throw new NotFoundException("Пользователь не найден в базе");
        }
    }

    public void removeFromFriends(Integer userId, Integer friendId) {
        if (getUserById(userId) != null && getUserById(friendId) != null) {
            getUserById(userId).deleteFriend(friendId);
            getUserById(friendId).deleteFriend(userId);
        } else {
            throw new NotFoundException("Пользователь не найден в базе");
        }
    }

    public Set<User> getFriendsOfUser(Integer userId) {
        Set<Long> friendsIds = getUserById(userId).getFriends();
        Set<User> friends = new HashSet<>();
        for (Long id : friendsIds) {
            friends.add(getUserById(id.intValue()));
        }
        return friends;
    }

    public Set<User> getCommonFriends(Integer userId, Integer otherUserId) {
        Set<Long> userFriends = getUserById(userId).getFriends();
        Set<Long> otherUserFriends = getUserById(otherUserId).getFriends();
        Set<Long> commonSet = new HashSet<>();
        if (userFriends != null && otherUserFriends != null
                && !userFriends.isEmpty() && !otherUserFriends.isEmpty()) {
            commonSet = userFriends.stream()
                    .filter(otherUserFriends::contains)
                    .collect(Collectors.toSet());
        }
        Set<User> commonFriends = new HashSet<>();
        for (Long id : commonSet) {
            commonFriends.add(getUserById(id.intValue()));
        }
        return commonFriends;
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        user.setId(id++);
        userStorage.add(user);
        return user;
    }

    public List<User> allUsers() {
        return userStorage.allUsers();
    }

    public User updateUser(User user) {
        userStorage.update(user);
        return user;
    }

}
