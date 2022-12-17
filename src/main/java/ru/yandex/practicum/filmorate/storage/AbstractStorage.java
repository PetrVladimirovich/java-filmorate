package ru.yandex.practicum.filmorate.storage;

public interface AbstractStorage<T> {
    void add(T t);
    void delete(int id);
    void update(T t);
}