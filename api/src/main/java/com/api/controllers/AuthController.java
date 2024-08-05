package com.api.controllers;

import com.api.dtos.*;
import com.api.services.AuthService;
import com.api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Tag(name = "Auth")
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    @Operation(
            summary = "Авторизация",
            description = "Авторизация пользователя и получение JWT токена"
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
    @Operation(
            summary = "Регистрация",
            description = "Регистрация пользователя без получения JWT токена"
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        return authService.register(request);
    }
    @Operation(
            summary = "Удаление",
            description = "<b>Необходим JWT токен в headers</b><br/>Удаление пользователя из базыданных"
    )
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete() {
        return userService.delete();
    }
    @Operation(
            summary = "Код активации",
            description = "<b>Необходим JWT токен в headers</b><br/>Создаёт на бэке код активации 100000"
    )
    @GetMapping("/activation")
    public ResponseEntity<?> getActivationCode() {
        return authService.getActivationCode();
    }
    @Operation(
            summary = "Активация",
            description = "<b>Необходим JWT токен в headers</b><br/>Активирует пользователя по JWT"
    )
    @PostMapping("/activation")
    public ResponseEntity<?> tryActivate(@RequestBody TryActivate tryActivate) {
        return authService.tryActivate(tryActivate);
    }
    @Operation(
            summary = "Код изменения пароля",
            description = "<b>Необходим JWT токен в headers</b><br/>Создаёт на бэке код смены пароля 100000"
    )
    @GetMapping("/changepassword")
    public ResponseEntity<?> getChangePasswordCode(@RequestParam("email") String email) {
        return authService.getChangePasswordCode(email);
    }
    @Operation(
            summary = "Проверка кода",
            description = "<b>Необходим JWT токен в headers</b><br/>Проверяет код"
    )
    @PostMapping("/changepassword/code")
    public ResponseEntity<?> checkIsCodeRight(@RequestBody ChangePasswordCodeRequest code) {
        return authService.isRightCode(code);
    }
    @Operation(
            summary = "Активация",
            description = "<b>Необходим JWT токен в headers</b><br/>Меняет пользователю пароль по JWT"
    )
    @PostMapping("/changepassword")
    public ResponseEntity<?> tryChangePassword(@RequestBody ChangePasswordRequest request) {
        return authService.changePasswordPage(request);
    }
    @Operation(
            summary = "Проверка существует ли почта",
            description = "Проверяет существует ли почта"
    )
    @PostMapping("/isEmailExist")
    public ResponseEntity<?> isEmailExists(@RequestBody EmailRequest email) {
        return authService.isEmailExists(email.getEmail());
    }
    @Operation(
            summary = "Проверка входа",
            description = "Проверяет вошёл ли пользователь"
    )
    @GetMapping("/isAuth")
    public ResponseEntity<?> isAuth() {
        return authService.isAuth();
    }
}
