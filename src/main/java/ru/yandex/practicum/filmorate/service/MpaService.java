package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

@Component
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<MpaRating> getAllMpa(){
        return mpaDbStorage.getAllMpa();
    }

    public MpaRating getMpa(Integer id){
        return mpaDbStorage.getMpa(id);
    }
}
