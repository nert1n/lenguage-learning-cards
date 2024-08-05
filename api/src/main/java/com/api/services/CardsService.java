package com.api.services;

import com.api.dtos.*;
import com.api.entities.Card;
import com.api.entities.Category;
import com.api.entities.User;
import com.api.repository.CardRepository;
import com.api.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.hibernate.query.spi.Limit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardsService {
    private final CardRepository cardRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    public ResponseEntity<?> getByAuthor() {
        User user = userService.getUser();
        List<Card> cards = cardRepository.findByAuthor(user);
        List<Card> favorites = getFavorites();
        List<Card> dictionary = userService.getDictionary();
        cards = cards.stream().filter(el -> !dictionary.contains(el)).toList();
        List<GetCardsResponse> response = cards.stream().map(el -> {
            if(favorites != null) {
                return new GetCardsResponse(el.getId(), el.getEngtext(), el.getRustext(), el.getImage(), favorites.contains(el));
            }
            return new GetCardsResponse(el.getId(), el.getEngtext(), el.getRustext(), el.getImage(), false);
        }).collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    private List<Card> getFavorites() {
        User user = userService.getUser();
        List<Category> categories = categoryRepository.findByUser(user);
        Optional<Category> favorites = categories.stream().filter(el -> el.getName().equals("Фразы")).findFirst();
        return favorites.map(Category::getCards).orElse(null);
    }
    public ResponseEntity<?> getAllCards(Integer limit) {
        List<Card> data;
        List<Card> dictionary = userService.getDictionary();
        List<Long> ids = dictionary.stream().map(Card::getId).collect(Collectors.toList());
        if(limit == null) data = cardRepository.findByIsPublic(true);
        else if (!ids.isEmpty()) data = cardRepository.findByIsPublicLatest(limit, true, ids);
        else data = cardRepository.findByIsPublicWithoutDictionary(limit, true);
        List<Card> favorites = getFavorites();
        data = data.stream().filter(el -> !dictionary.contains(el)).toList();
        if(favorites == null) return new ResponseEntity<>(data.stream().map(el -> new GetCardsResponse(
                        el.getId(),
                        el.getEngtext(),
                        el.getRustext(),
                        el.getImage(),
                        false
                )
        ), HttpStatus.OK);
        else return new ResponseEntity<>(data.stream().map(el -> new GetCardsResponse(
                        el.getId(),
                        el.getEngtext(),
                        el.getRustext(),
                        el.getImage(),
                        favorites.contains(el)
                )
        ), HttpStatus.OK);
    }
    public Card getCardById(Long id) {
        return cardRepository.findById(id).get();
    }
    public ResponseEntity<?> createCard(CreateCardRequest request) throws IOException {
        Card card = new Card();
        card.setEngtext(request.getEngtext());
        card.setRustext(request.getRustext());
        card.setImage(request.getImage().getBytes());
        card.setAuthor(userService.getUser());
        card.setPublic(false);
        userService.incrementCreatedCards();
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public ResponseEntity<?> createCardWithoutImage(CreateCardWithoutImageRequest request) throws IOException {
        Card card = new Card();
        card.setEngtext(request.getEngtext());
        card.setRustext(request.getRustext());
        card.setAuthor(userService.getUser());
        card.setPublic(false);
        userService.incrementCreatedCards();
        cardRepository.save(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public Card createCategoryCard(CreateCardRequest request) throws IOException {
        Card card = new Card();
        card.setEngtext(request.getEngtext());
        card.setRustext(request.getRustext());
        card.setImage(request.getImage().getBytes());
        card.setAuthor(userService.getUser());
        userService.incrementCreatedCards();
        cardRepository.save(card);
        return card;
    }
    public Card createCategoryCard(CreateCardWithoutImageRequest request) throws IOException {
        Card card = new Card();
        card.setEngtext(request.getEngtext());
        card.setRustext(request.getRustext());
        card.setAuthor(userService.getUser());
        userService.incrementCreatedCards();
        cardRepository.save(card);
        return card;
    }
    public ResponseEntity<?> updateCard(UpdateCardRequest request) {
        Optional<Card> card = cardRepository.findById(request.getId());
        if(card.isEmpty()) return new ResponseEntity<>("Card does not exists", HttpStatus.BAD_REQUEST);
        if(card.get().getAuthor() != userService.getUser()) return new ResponseEntity<>("You are not author of this card", HttpStatus.BAD_REQUEST);
        Card newCard = card.get();
        newCard.setEngtext(request.getEngtext());
        newCard.setRustext(request.getRustext());
        try {
            cardRepository.save(newCard);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Unhandled exeption", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> updateImage(UpdateImageRequest request) throws IOException {
        Optional<Card> card = cardRepository.findById(request.getId());
        if(card.isEmpty()) return new ResponseEntity<>("Card does not exists", HttpStatus.BAD_REQUEST);
        if(card.get().getAuthor() != userService.getUser()) return new ResponseEntity<>("You are not author of this card", HttpStatus.BAD_REQUEST);
        Card newCard = card.get();
        newCard.setImage(request.getImage().getBytes());
        try {
            cardRepository.save(newCard);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Unhandled exeption", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> delete(DeleteCardRequest request) {
        UserDetails user = userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Card card = getCardById(request.getId());
        if (card == null) {
            return new ResponseEntity<>("Card does not exists", HttpStatus.BAD_REQUEST);
        }
        if(!Objects.equals(card.getAuthor().getUsername(), user.getUsername()) ||
                user.getAuthorities().stream().map(Object::toString).toList().contains("ROLE_ADMIN")
        ) {
            return new ResponseEntity<>("You do not have permission to this", HttpStatus.BAD_REQUEST);
        }
        cardRepository.delete(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public ResponseEntity<?> getCard(long id) {
        Optional<Card> card = cardRepository.findById(id);
        if(card.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new GetCardsResponse(
                card.get().getId(),
                card.get().getEngtext(),
                card.get().getRustext(),
                card.get().getImage(),
                false
        ), HttpStatus.OK);
    }
    public ResponseEntity<?> deleteImage(DeleteImageRequest request) {
        Optional<Card> card = cardRepository.findById(request.getId());
        if(card.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Card newCard = card.get();
        newCard.setImage(null);
        cardRepository.save(newCard);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public ResponseEntity<?> removeFromDictionary(long id) {
        Optional<Card> card = cardRepository.findById(id);
        if(card.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            userService.removeFromDictionary(card.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> addToDictionary(long id) {
        Optional<Card> card = cardRepository.findById(id);
        if(card.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            userService.addToDictionary(card.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
