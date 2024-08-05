package com.api.services;

import com.api.dtos.*;
import com.api.entities.Card;
import com.api.entities.Category;
import com.api.entities.User;
import com.api.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final CardsService cardsService;
    public ResponseEntity<?> getCategoryById(Long id) {
        User user = userService.getUser();
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else return new ResponseEntity<>(category.get().getName(), HttpStatus.OK);
    }
    public ResponseEntity<?> getCategoryByName(String name) {
        User user = userService.getUser();
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Category> categories = categoryRepository.findByUser(user);
        Optional<Category> category = categories.stream().filter(el -> Objects.equals(el.getName(), name)).findFirst();
        if(category.isPresent()) return new ResponseEntity<>(category.get().getId(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    public ResponseEntity<?> getCategories(Integer limit) {
        User user = userService.getUser();
        if (user == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (limit != null) {
            List<Category> data = categoryRepository.findByUser(user);
            data = data.stream().map(el -> {
                if(el.getName().equals("Фразы") && el.getCards().isEmpty()) return null;
                else return el;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            data = data.stream().limit(limit).toList();
            return new ResponseEntity<>(data.stream().map(el -> new GetCategoriesResponse(
                            el.getId(),
                            el.getName(),
                            el.getCards().stream().map(card -> new GetCardsResponse(card.getId(), card.getEngtext(), card.getRustext(), card.getImage(), false)).collect(Collectors.toList())
                    )
            ), HttpStatus.OK);
        }
        return new ResponseEntity<>(categoryRepository.findByUser(user).stream().map(el -> new GetCategoriesResponse(
                        el.getId(),
                        el.getName(),
                        el.getCards().stream().map(card -> new GetCardsResponse(card.getId(), card.getEngtext(), card.getRustext(), card.getImage(), false)).collect(Collectors.toList())
                )
        ), HttpStatus.OK);
    }
    public ResponseEntity<?> createCategory(CreateCategoryRequest request) {
        Optional<Category> categoryOptional = categoryRepository.findByName(request.getName());
        if(categoryOptional.isPresent() && userService.getUser() == categoryOptional.get().getUser()) {
            return new ResponseEntity<>("Category with this name already exists", HttpStatus.BAD_REQUEST);
        }
        userService.incrementCreatedCategories();
        Category category = new Category();
        category.setName(request.getName());
        category.setCards(Arrays.stream(request.getCard_ids()).mapToObj(cardsService::getCardById).collect(Collectors.toList()));
        category.setUser(userService.getUser());
        Category newCategory = categoryRepository.save(category);
        return new ResponseEntity<>(newCategory.getId(), HttpStatus.OK);
    }
    public ResponseEntity<?> updateCategory(UpdateCategoryRequest request) {
        Optional<Category> category = categoryRepository.findById(request.getCategory_id());
        if(category.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.BAD_REQUEST);
        }
        if(category.get().getUser() != userService.getUser() || Objects.equals(category.get().getName(), "Фразы")) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Category newCategory = category.get();
        newCategory.setCards(
                Arrays.stream(request.getCard_ids()).map(cardsService::getCardById).collect(Collectors.toList())
        );
        try {
            categoryRepository.save(newCategory);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Unhandled exception", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> addExistsCard(AddExistsRequest request) {
        Optional<Category> categoryOptional = categoryRepository.findById(request.getCat_id());
        if(categoryOptional.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Card card = cardsService.getCardById(request.getCard_id());
        if(card == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Category category = categoryOptional.get();
        List<Card> cards = category.getCards();
        cards.add(card);
        category.setCards(cards);
        try {
            categoryRepository.save(category);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> addToCategory(AddToCategoryRequest request) throws IOException {
        Optional<Category> category = categoryRepository.findById(request.getCatid());
        if(category.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.BAD_REQUEST);
        }
        CreateCardRequest createCardRequest = new CreateCardRequest(request.getEngtext(), request.getRustext(), request.getImage());
        Card card = cardsService.createCategoryCard(createCardRequest);
        Category newCategory = category.get();
        List<Card> cards = newCategory.getCards();
        cards.add(card);
        newCategory.setCards(cards);
        try {
            categoryRepository.save(newCategory);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("This card is already in category", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> addToCategoryWithoutImage(AddToCatWithoutImageRequest request) throws IOException {
        Optional<Category> category = categoryRepository.findById(request.getCatid());
        if(category.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.BAD_REQUEST);
        }
        CreateCardWithoutImageRequest createCardRequest = new CreateCardWithoutImageRequest(request.getEngtext(), request.getRustext());
        Card card = cardsService.createCategoryCard(createCardRequest);
        Category newCategory = category.get();
        List<Card> cards = newCategory.getCards();
        cards.add(card);
        newCategory.setCards(cards);
        try {
            categoryRepository.save(newCategory);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("This card is already in category", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> deleteOne(DeleteOneCardRequest request) {
        Optional<Category> categoryOptional = categoryRepository.findById(request.getCategory_id());
        if(categoryOptional.isEmpty()) {
            return new ResponseEntity<>("Category does not exists", HttpStatus.BAD_REQUEST);
        }
        Category category = categoryOptional.get();
        Card card = cardsService.getCardById(request.getCard_id());
        if(card == null) {
            return new ResponseEntity<>("Card does not exists", HttpStatus.BAD_REQUEST);
        }
        List<Card> cards = category.getCards();
        if(!cards.contains(card)) return new ResponseEntity<>("Categoriy does not contains card", HttpStatus.BAD_REQUEST);
        cards.remove(card);
        if(cards.isEmpty() && Objects.equals(category.getName(), "Фразы")) {
            categoryRepository.delete(categoryOptional.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        category.setCards(cards);
        categoryRepository.save(category);
        try {
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Card does not found in category", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<?> delete(DeleteCategoryRequest request) {
        Optional<Category> category = categoryRepository.findById(request.getId());
        if (category.isEmpty()) {
            return new ResponseEntity<>("Cat does not exists", HttpStatus.BAD_REQUEST);
        }
        Category empty = category.get();
        empty.setCards(new ArrayList<>());
        categoryRepository.save(empty);
        categoryRepository.delete(empty);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public ResponseEntity<?> getAllCards(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isEmpty() || categoryOptional.get().getUser() != userService.getUser()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Category category = categoryOptional.get();
        List<Card> cards = category.getCards();
        List<Card> dictionary = userService.getDictionary();
        cards = cards.stream().filter(el -> !dictionary.contains(el)).toList();
        return new ResponseEntity<>(cards.stream().map(el -> new GetCardsResponse(el.getId(), el.getEngtext(), el.getRustext(), el.getImage(), Objects.equals(category.getName(), "Фразы"))).collect(Collectors.toList()), HttpStatus.OK);
    }
    public List<Card> getCards(Long id) {
        return categoryRepository.findById(id).map(Category::getCards).orElse(null);
    }
}
