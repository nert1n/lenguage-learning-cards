package com.api.controllers;

import com.api.dtos.*;
import com.api.entities.Category;
import com.api.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Categories")
@RequestMapping("/api/categories")
public class CategoriesController {
    private final CategoryService categoryService;
    @Operation(
            summary = "Получение всех категорий",
            description = "<b>Нужен JWT токен в headers</b><br/>Возвращает все категории юзера."
    )
    @GetMapping("/getall")
    public ResponseEntity<?> getAll(@RequestParam(value = "limit", required = false) Integer limit) {
        return categoryService.getCategories(limit);
    }
    @Operation(
            summary = "Создание категории",
            description = "<b>Нужен JWT токен в headers</b><br/>Создаёт категорию по названию и с теми карточками, что указаны в поле cards_id."
    )
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }
    @Operation(
            summary = "Обновление категории",
            description = "<b>Нужен JWT токен в headers</b><br/>Принимает новый массив карточек, который заменит предыдущий."
    )
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateCategoryRequest request) {
        return categoryService.updateCategory(request);
    }
    @Operation(
            summary = "Добавление карточки в категорию",
            description = "<b>Нужен JWT токен в headers</b><br/>Добавляет 1 карточку в категорию."
    )
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> add(@ModelAttribute AddToCategoryRequest request) throws IOException {
        return categoryService.addToCategory(request);
    }
    @PostMapping("/addWithoutImage")
    public ResponseEntity<?> addWithoutImage(@RequestBody AddToCatWithoutImageRequest request) throws IOException {
        return categoryService.addToCategoryWithoutImage(request);
    }
    @Operation(
            summary = "Добавление существующей карточки в категорию",
            description = "<b>Нужен JWT токен в headers</b><br/>Добавляет 1 существующую карточку в категорию."
    )
    @PostMapping("/addExists")
    public ResponseEntity<?> addExists(@RequestBody AddExistsRequest request) {
        return categoryService.addExistsCard(request);
    }
    @Operation(
            summary = "Удаление 1 карточки",
            description = "<b>Необходим JWT токен в headers</b><br/>Удаление 1 карточки из категории."
    )
    @DeleteMapping("/delone")
    public ResponseEntity<?> deleteOne(@RequestBody DeleteOneCardRequest request) {
        return categoryService.deleteOne(request);
    }
    @Operation(
            summary = "Удаление категории",
            description = "<b>Необходим JWT токен в headers</b><br/>Удаление категории из бд."
    )
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody DeleteCategoryRequest request) {
        return categoryService.delete(request);
    }
    @Operation(
            summary = "Получение всех карточек",
            description = "<b>Необходим JWT токен в headers</b><br/>Получение всех карточек категории."
    )
    @GetMapping("/getCards")
    public ResponseEntity<?> getCards(@RequestParam("id") Long id) {
        return categoryService.getAllCards(id);
    }
    @Operation(
            summary = "Получение категории по id",
            description = "<b>Необходим JWT токен в headers</b><br/>Получение категории по id."
    )
    @GetMapping
    public ResponseEntity<?> getCategoryById(@RequestParam("id") Long id) {
        return categoryService.getCategoryById(id);
    }
    @Operation(
            summary = "Получение категории по name",
            description = "<b>Необходим JWT токен в headers</b><br/>Получение категории по name."
    )
    @GetMapping("/getByName")
    public ResponseEntity<?> getCategoryByName(@RequestParam("name") String name) {
        return categoryService.getCategoryByName(name);
    }
}
