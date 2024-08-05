package com.api.controllers;

import com.api.dtos.GiveExpRequest;
import com.api.dtos.IncrementSwipedRequest;
import com.api.services.CardsService;
import com.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User")
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final CardsService cardsService;
    @Operation(
            summary = "Получение информации о пользователе",
            description = "<b>Необходим JWT токен в headers</b><br/>Возвращает UserInfoResponse."
    )
    @GetMapping
    public ResponseEntity<?> getUserInfo() {
        return userService.getUserInfo();
    }
    @Operation(
            summary = "Увеличение пролистанных",
            description = "<b>Необходим JWT токен в headers</b><br/>Увеличивает статистику пролистанных карточек."
    )
    @PostMapping("/incrementSwiped")
    public ResponseEntity<?> incSwiped(@RequestBody IncrementSwipedRequest request) {
        return userService.addSwiped(request.getSwiped());
    }
    @PostMapping("/incrementSwipedCategories")
    public ResponseEntity<?> incSwipedCategories(@RequestBody IncrementSwipedRequest request) {
        return userService.addSwipedCategories(request.getSwiped());
    }
    @Operation(
            summary = "Выдача опыта",
            description = "<b>Необходим JWT токен в headers</b><br/>Принимает в виде float количество опыта (1.0 - 1 уровень), увеличивает опыт пользователя."
    )
    @PostMapping("/giveExp")
    public ResponseEntity<?> giveExp(@RequestBody GiveExpRequest request) {
        return userService.giveExp(request);
    }
    @Operation(
            summary = "Добавление в словарь",
            description = "<b>Необходим JWT токен в headers</b><br/>Добавляет карточку в словарь."
    )
    @PostMapping("/addToDictionary")
    public ResponseEntity<?> addToDictionary(@RequestParam("id") long id) {
        return cardsService.addToDictionary(id);
    }
    @Operation(
            summary = "Удаление карточки",
            description = "<b>Необходим JWT токен в headers</b><br/>Удаление карточки из словаря."
    )
    @PostMapping("/removeFromDictionary")
    public ResponseEntity<?> removeFromDictionary(@RequestParam("id") long id) {
        return cardsService.removeFromDictionary(id);
    }
    @Operation(
            summary = "Получение словаря",
            description = "<b>Необходим JWT токен в headers</b><br/>Возвращает список карточек из словаря."
    )
    @GetMapping("/getDictionary")
    public ResponseEntity<?> getDictionary() {
        return userService.getDicionaryReq();
    }
}
