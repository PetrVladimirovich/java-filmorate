package ru.yandex.practicum.filmorate.storage;

public interface AbstractStorage<T> {
    T add(T t);
    void delete(T id);
    T update(T t);
}