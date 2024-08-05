package com.api.repository;

import com.api.entities.Card;
import com.api.entities.User;
import io.micrometer.common.KeyValues;
import org.hibernate.query.spi.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    @Override
    List<Card> findAll();
    public Optional<Card> findById(Long id);
    public Card findByEngtext(String engtext);
    public List<Card> findByAuthor(User author);
    public List<Card> findByIsPublic(boolean IsPublic);
    @Query(value = "SELECT * FROM cards WHERE is_public = :isPublic order by id desc limit :limit", nativeQuery = true)
    public List<Card> findByIsPublicWithoutDictionary(@Param("limit") int limit, @Param("isPublic") boolean isPublic);
    @Query(value = "SELECT * FROM cards WHERE is_public = :isPublic AND id NOT IN :ids order by id desc limit :limit", nativeQuery = true)
    public List<Card> findByIsPublicLatest(@Param("limit") int limit, @Param("isPublic") boolean isPublic, @Param("ids") List<Long> ids);
    @Query(value = "select * from cards order by id desc limit :limit", nativeQuery = true)
    List<Card> findLatest(@Param("limit") int limit);
}
