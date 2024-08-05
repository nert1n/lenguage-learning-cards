package com.api.services;

import com.api.dtos.GetAllTrainsResponse;
import com.api.dtos.GetCardsResponse;
import com.api.entities.Card;
import com.api.entities.Train;
import com.api.entities.User;
import com.api.repository.TrainRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TrainService {
    private final TrainRepository trainRepository;
    private final UserService userService;
    public ResponseEntity<?> getName(long id) {
        Optional<Train> train = trainRepository.findById(id);
        if (train.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(train.get().getName(), HttpStatus.OK);
    }
    public ResponseEntity<?> start(long id) {
        Optional<Train> train = trainRepository.findById(id);
        if(train.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<Card> cards = train.get().getCards();
        Collections.shuffle(cards);
        List<Card> dictionary = userService.getDictionary();
        cards = cards.stream().filter(el -> !dictionary.contains(el)).toList();
        return new ResponseEntity<>(cards.stream().map(el -> new GetCardsResponse(el.getId(), el.getEngtext(), el.getRustext(), el.getImage(), false)), HttpStatus.OK);
    }
    public ResponseEntity<?> getTrains(Integer limit) {
        List<Train> trains;
        if(limit == null) trains = trainRepository.findAll();
        else trains = trainRepository.getAll(limit);
        return new ResponseEntity<>(trains.stream().map(el -> new GetAllTrainsResponse(el.getId(), el.getName())), HttpStatus.OK);
    }
}
