package com.api.services;

import com.api.dtos.GetCardsResponse;
import com.api.dtos.GiveExpRequest;
import com.api.dtos.UserInfoResponse;
import com.api.entities.Card;
import com.api.entities.Category;
import com.api.entities.User;
import com.api.repository.CardRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final CategoryRepository categoryRepository;
    private final CardRepository cardRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream().map(el -> new SimpleGrantedAuthority(el.getName())).collect(Collectors.toList())
        );
    }
    public User getUser() {
        return userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivate(false);
        user.setLevel(1);
        user.setRoles(List.of(roleService.getUserRole()));
        userRepository.save(user);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public void setActivation(User user) {
        user.setActivate(true);
        userRepository.save(user);
    }
    public void setActivationCode(String code, User user) {
        user.setActivationCode(code);
        userRepository.save(user);
    }
    public void setChangePasswordCode(String code, User user) {
        user.setChangePasswordCode(code);
        userRepository.save(user);
    }
    public void setCanChange(User user, boolean value) {
        user.setCanChangePassword(value);
        userRepository.save(user);
    }
    public void changePasswordPage(String newPassword, User user) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    public ResponseEntity<?> delete() {
        try {
            userRepository.delete(getUser());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> giveExp(GiveExpRequest request) {
        User user = getUser();
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            user.setLevel(user.getLevel() + request.getExp());
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> getUserInfo() {
        User user = getUser();
        if(user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getLevel(),
                user.getSwiped(),
                user.getSwipedCategories(),
                user.getCreatedCards(),
                user.getCreatedCategories()
        ), HttpStatus.OK);
    }
    public void incrementCreatedCards() {
        User user = getUser();
        user.setCreatedCards(user.getCreatedCards() + 1);
        userRepository.save(user);
    }
    public void incrementCreatedCategories() {
        User user = getUser();
        user.setCreatedCategories(user.getCreatedCategories() + 1);
        userRepository.save(user);
    }
    public void incrementSwiped(int amount) {
        User user = getUser();
        user.setSwiped(user.getSwiped() + amount);
        userRepository.save(user);
    }
    public void incrementSwipedCategories(int amount) {
        User user = getUser();
        user.setSwipedCategories(user.getSwipedCategories() + amount);
        userRepository.save(user);
    }
    public ResponseEntity<?> addSwiped(int amount) {
        try {
            incrementSwiped(amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> addSwipedCategories(int amount) {
        try {
            incrementSwipedCategories(amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    public List<Card> getDictionary() {
        User user = getUser();
        if(user == null) return new ArrayList<>();
        else return user.getCards();
    }
    public ResponseEntity<?> getDicionaryReq() {
        List<Card> cards = getDictionary();
        return new ResponseEntity<>(cards.stream().map(el -> new GetCardsResponse(
                el.getId(),
                el.getEngtext(),
                el.getRustext(),
                el.getImage(),
                false
        )), HttpStatus.OK);
    }
    public void removeFromDictionary(Card card) {
        User user = getUser();
        List<Card> dictionary = getDictionary();
        dictionary.remove(card);
        user.setCards(dictionary);
        userRepository.save(user);
    }
    public void addToDictionary(Card card) {
        User user = getUser();
        List<Card> dictionary = getDictionary();
        dictionary.add(card);
        user.setCards(dictionary);
        clearFromCard(card.getId());
        userRepository.save(user);
    }
    public void clearFromCard (Long card_id) {
        List<Category> categories = categoryRepository.findByUser(getUser());
        categories.forEach(el -> {
            List<Card> cards = el.getCards();
            Optional<Card> card = cardRepository.findById(card_id);
            card.ifPresent(cards::remove);
            el.setCards(cards);
        });
        categoryRepository.saveAll(categories);
    }
}
