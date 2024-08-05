package com.api.services;

import com.api.dtos.*;
import com.api.entities.User;
import com.api.repository.UserRepository;
import com.api.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    public ResponseEntity<?> login(LoginRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    req.getUsername(),
                    req.getPassword()
            ));
        } catch (AuthenticationException ex) {
            return new ResponseEntity<>("Authorization error", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(req.getUsername());
        return new ResponseEntity<>(jwtUtils.generateToken(userDetails), HttpStatus.OK);
    }
    public ResponseEntity<?> register(RegistrationRequest req) {
        try {
            userService.loadUserByUsername(req.getUsername());
            userService.getUserByEmail(req.getEmail());
            if(userService.getUserByEmail(req.getEmail()) == null) throw new Exception();
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            User user = new User();
            user.setUsername(req.getUsername());
            user.setPassword(req.getPassword());
            user.setEmail(req.getEmail());
            user.setActivate(false);
            user.setActivationCode("no");
            user.setCanChangePassword(false);
            user.setChangePasswordCode("no");
            user.setSwiped(0);
            user.setSwipedCategories(0);
            user.setCreatedCards(0);
            user.setCreatedCategories(0);
            userService.createUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
    public ResponseEntity<?> tryActivate(TryActivate tryActivate) {
        if(userService.getUser().getActivationCode().equals(tryActivate.code)) {
            userService.setActivation(userService.getUser());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<?> getActivationCode() {
        if(SecurityContextHolder.getContext().getAuthentication().getName() == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            userService.setActivationCode("100000", userService.getUser());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> getChangePasswordCode(String email) {
        if(userService.getUserByEmail(email) == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            userService.setChangePasswordCode("100000", userService.getUserByEmail(email));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> isRightCode(ChangePasswordCodeRequest request) {
        if(userService.getUserByEmail(request.getEmail()) != null) {
            userService.setCanChange(userService.getUserByEmail(request.getEmail()), true);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<?> changePasswordPage(ChangePasswordRequest request) {
        User user = userService.getUserByEmail(request.getEmail());
        if(user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(user.isCanChangePassword()) {
            userService.setCanChange(user, false);
            userService.changePasswordPage(request.getNewPassword(), user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> isEmailExists(String email) {
        User user = userService.getUserByEmail(email);
        if(user == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else return new ResponseEntity<>(HttpStatus.OK);
    }
    public ResponseEntity<?> isAuth() {
        if(userService.getUser() != null) return new ResponseEntity<>(true, HttpStatus.OK);
        else return new ResponseEntity<>(false, HttpStatus.OK);
    }
}
