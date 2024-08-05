package com.api.controllers;

import com.api.services.TrainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Train")
@RestController
@RequestMapping("/api/train")
@AllArgsConstructor
public class TrainController {
    private final TrainService trainService;
    @Operation(
            summary = "Получение имени тренировки",
            description = "<b>Необходим JWT токен в headers</b><br/>Возвращает название тренировки по id."
    )
    @GetMapping("/getName")
    public ResponseEntity<?> getName(@RequestParam("id") long id) {
        return trainService.getName(id);
    }
    @Operation(
            summary = "Начало тренировки",
            description = "<b>Необходим JWT токен в headers</b><br/>Возвращает массив перемешанных карточек по id категории."
    )
    @GetMapping("/start")
    public ResponseEntity<?> start(@RequestParam("id") long id) {
        return trainService.start(id);
    }
    @Operation(
            summary = "Получение всех тренировок",
            description = "<b>Необходим JWT токен в headers</b><br/>Возвращает массив тренировок."
    )
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "limit", required = false) Integer limit) { return trainService.getTrains(limit); }
}
