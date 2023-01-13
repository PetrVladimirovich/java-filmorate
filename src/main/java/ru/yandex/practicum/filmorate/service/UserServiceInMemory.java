package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserServiceInMemory implements UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    public void addToFriends(Integer userId, Integer friendId) {
        if (getUserById(userId) != null && getUserById(friendId) != null) {
            getUserById(userId).addFriend(friendId);
            getUserById(friendId).addFriend(userId);
        } else {
            throw new NotFoundException("Пользователь не найден в базе");
        }
    }

    public void deleteFromFriends(Integer userId, Integer friendId) {
        if (getUserById(userId) != null && getUserById(friendId) != null) {
            getUserById(userId).deleteFriend(friendId);
            getUserById(friendId).deleteFriend(userId);
        } else {
            throw new NotFoundException("Пользователь не найден в базе");
        }
    }

    @Override
    public Set<User> friendsOfUser(Integer userId) {
        Set<Long> friendsIds = getUserById(userId).getFriends();
        Set<User> friends = new HashSet<>();
        for (Long id : friendsIds) {
            friends.add(getUserById(id.intValue()));
        }
        return friends;
    }

    @Override
    public Set<User> commonFriends(Integer userId, Integer otherUserId) {
        Set<Long> userFriends = getUserById(userId).getFriends();
        Set<Long> otherUserFriends = getUserById(otherUserId).getFriends();
        Set<Long> commonList = new HashSet<>();
        if (userFriends != null && otherUserFriends != null
                && !userFriends.isEmpty() && !otherUserFriends.isEmpty()) {
            commonList = userFriends.stream()
                        .filter(otherUserFriends::contains)
                        .collect(Collectors.toSet());
        }
        Set<User> commonFriends = new HashSet<>();
        for (Long id : commonList) {
            commonFriends.add(getUserById(id.intValue()));
        }
        return commonFriends;
    }

    private User getUserById(int id) {
        return inMemoryUserStorage.allUsers().get(id);
    }



}
