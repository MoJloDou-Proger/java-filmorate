package ru.yandex.practicum.filmorate.controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

import static ru.yandex.practicum.filmorate.constants.Constants.USER_DB_STORAGE;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier(USER_DB_STORAGE) UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addFriend(Integer id, Integer friendId) {
        return userStorage.addFriend(id, friendId);
    }

    public String deleteFriend(Integer id, Integer friendId){
        return userStorage.deleteFriend(id, friendId);
    }

    public List<User> receiveFriends(Integer id){
        return userStorage.receiveFriends(id);
    }

    public List<User> receiveCommonFriends(Integer id, Integer otherId) {
        return userStorage.receiveCommonFriends(id, otherId);
    }

    public List<User> allUsers(){
        return userStorage.allUsers();
    }

    public User findUser(int id){
        return userStorage.findUser(id);
    }

    public User createUser(User user){
        return userStorage.createUser(user);
    }

    public User updateUser(User user){
        return userStorage.updateUser(user);
    }

    public String deleteUser(int id){
        return userStorage.deleteUser(id);
    }
}
