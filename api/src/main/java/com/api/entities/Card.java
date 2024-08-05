package com.api.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String engtext;
    private String rustext;
    @ManyToOne
    private User author;
    private boolean isPublic;
    @Column(name = "image", columnDefinition = "bytea")
    private byte[] image;
}
