package com.api.controllers;

import com.api.dtos.*;
import com.api.entities.Card;
import com.api.entities.Category;
import com.api.repository.CategoryRepository;
import com.api.services.CardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cards")
@Tag(name = "Cards")
public class CardsController {
    private final CardsService cardsService;
    @Operation(
            summary = "Получение карточек по юзеру",
            description = "<b>Необходим JWT токен в headers</b><br/>Возвращает массив по юзеру с полями engtext и rustext."
    )
    @GetMapping("/byAuthor")
    public ResponseEntity<?> getCardsByAuthor() {
        return cardsService.getByAuthor();
    }
    @Operation(
            summary = "Получение карточек",
            description = "<b>Необходим JWT токен в headers</b><br/>Возвращает массив карточек с полями engtext и rustext."
    )
    @GetMapping
    public ResponseEntity<?> getCards(@RequestParam(value = "limit", required = false) Integer limit) {
        return cardsService.getAllCards(limit);
    }
    @Operation(
            summary = "Получение карточки",
            description = "<b>Необходим JWT токен в headers</b><br/>Возвращает карточку с полями engtext и rustext."
    )
    @GetMapping("/getCard")
    public ResponseEntity<?> getCard(@RequestParam("id") Long id) {
        return cardsService.getCard(id);
    }
    @Operation(
            summary = "Создание карточек",
            description = "<b>Необходим JWT токен в headers</b><br/>Создаёт карточку из полей engtext и rustext."
    )
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(@ModelAttribute CreateCardRequest request) throws IOException {
         return cardsService.createCard(request);
    }
    @PostMapping("/createWithoutImage")
    public ResponseEntity<?> createWithOutImage(@RequestBody CreateCardWithoutImageRequest request) throws IOException {
        return cardsService.createCardWithoutImage(request);
    }
    @Operation(
            summary = "Обновление карточки",
            description = "<b>Необходим JWT токен в headers</b><br/>Обновляет перевод и оригинал карточки."
    )
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateCardRequest request) {
        return cardsService.updateCard(request);
    }
    @Operation(
            summary = "Обновление изображения карточки",
            description = "<b>Необходим JWT токен в headers</b><br/>Обновляет image карточки."
    )
    @PostMapping(value = "/updateImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@ModelAttribute UpdateImageRequest request) throws IOException {
        return cardsService.updateImage(request);
    }
    @Operation(
            summary = "Удаление карточки",
            description = "<b>Необходим JWT токен в headers</b><br/>Удаление карточки из бд."
    )
    @PostMapping("/deleteImage")
    public ResponseEntity<?> deleteImage(@RequestBody DeleteImageRequest request) {
        return cardsService.deleteImage(request);
    }
    @Operation(
            summary = "Удаление карточки",
            description = "<b>Необходим JWT токен в headers</b><br/>Удаление карточки из бд."
    )
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody DeleteCardRequest request) {
        return cardsService.delete(request);
    }
}
