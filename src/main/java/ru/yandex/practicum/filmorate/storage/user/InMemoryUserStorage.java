package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IdNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constants.Constants.IN_MEMORY_USER_STORAGE;
import static ru.yandex.practicum.filmorate.validation.Validation.validateUser;

@Slf4j
@Component
@Qualifier(IN_MEMORY_USER_STORAGE)
public class InMemoryUserStorage implements UserStorage{
    private final List<User> users;
    private int userId = 1;

    public InMemoryUserStorage(List<User> users) {
        this.users = users;
    }

    @Override
    public User createUser(User user){
        log.info("Получен POST-запрос с объектом User: {}", user);
        user.setId(userId);
        validateUser(user);
        users.add(user);
        log.info("Пользователь {} c id={} добавлен", user.getName(), userId);
        increaseUserId();
        return user;
    }

    @Override
    public User updateUser(User user){
        log.info("Получен PUT-запрос с объектом User: {}", user);
        validateUser(user);
        int updateId = user.getId();
        for (User u : users) {
            if (u.getId() == updateId){
            users.set(users.indexOf(u), user);
            log.info("Пользователь c id={} обновлён", updateId);
            return user;
            }
        }
        throw new IdNotFoundException("Пользователь с id=" + updateId + " не найден");
    }

    @Override
    public String deleteUser(int id){
        for (User u : users) {
            if (u.getId() == id){
                users.remove(u);
                log.info("Пользователь c id={} удалён", id);
                return String.format("Пользователь c id=%s удалён", id);
            }
        }
        throw new IdNotFoundException("Пользователь с id=" + id + " не найден");
    }

    @Override
    public User findUser(int id){
        for (User u : users){
            if (u.getId() == id){
                return u;
            }
        }
        throw new IdNotFoundException("Пользователь с id=" + id + " не найден");
    }

    @Override
    public List<User> allUsers(){
        log.info("Получен GET-запрос на all");
        return new ArrayList<>(users);
    }
    @Override
    public String addFriend(Integer id, Integer friendId) {
        if (isUsersNotNull(id, friendId)){
            getFriendsId(id).add(friendId);
            getFriendsId(friendId).add(id);
            return String.format("Пользователь с id=%s добавлен в список друзей.", friendId);
        }
        throw new IdNotFoundException("Пользователь с указанным id не найден");
    }
    @Override
    public String deleteFriend(Integer id, Integer friendId){
        if (isUsersNotNull(id, friendId)){
            getFriendsId(id).remove(friendId);
            getFriendsId(friendId).remove(id);
            return String.format("Пользователь с id=%s удалён из списка друзей.", friendId);
        }
        throw new IdNotFoundException("Пользователь с указанным id не найден");
    }
    @Override
    public List<User> receiveFriends(Integer id){
        if (findUser(id) != null){
            Set<Integer> friendsId = getFriendsId(id);
            return getListOfFriends(friendsId);
        }
        throw new IdNotFoundException("Пользователь с указанным id не найден");
    }
    @Override
    public List<User> receiveCommonFriends(Integer id, Integer otherId) {
        if (isUsersNotNull(id, otherId)){
            Set<Integer> common = new HashSet<>(getFriendsId(id));
            common.retainAll(getFriendsId(otherId));
            return getListOfFriends(common);
        }
        throw new IdNotFoundException("Пользователь с указанным id не найден");
    }

    private List<User> getListOfFriends(Set<Integer> friendsId){
        List<User> listOfFriends = new ArrayList<>();
        if (friendsId != null && !friendsId.isEmpty()){
            listOfFriends = allUsers().stream()
                    .filter(u -> friendsId.stream()
                            .filter(fr -> u.getId() == fr)
                            .isParallel())
                    .collect(Collectors.toList());
        }
        return listOfFriends;
    }

    @Override
    public boolean isUsersNotNull(Integer id, Integer otherId){
        return findUser(id) != null && findUser(otherId) != null;
    }

    public Set<Integer> getFriendsId(Integer id){
        return findUser(id).getFriends();
    }

    private void increaseUserId(){
        userId++;
    }
}
