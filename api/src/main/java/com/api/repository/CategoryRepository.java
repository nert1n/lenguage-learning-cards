package com.api.repository;

import com.api.entities.Category;
import com.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Override
    List<Category> findAll();
    public List<Category> findByUser(User user);
    public Optional<Category> findByName(String name);
    public Optional<Category> findById(Long id);
}
