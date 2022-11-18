package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.controller.service.MpaService;

import java.util.List;

import static ru.yandex.practicum.filmorate.validation.Validation.checkId;

@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @Autowired
    public MpaController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public List<MpaRating> getAllMpa(){
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    public MpaRating getMpa(@PathVariable Integer id){
        checkId(id);
        return mpaService.getMpa(id);
    }
}
