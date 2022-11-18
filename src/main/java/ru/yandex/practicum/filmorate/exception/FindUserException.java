package ru.yandex.practicum.filmorate.exception;

public class FindUserException extends RuntimeException{
    public FindUserException(String message) {
        super(message);
    }
}
