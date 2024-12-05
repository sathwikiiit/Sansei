package com.sansei.shop.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sansei.shop.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryAndTagsContainingIgnoreCaseAndNameContainingIgnoreCase(String category, String tags,
            String searchString, Pageable pageable);

    Page<Product> findByCategoryAndNameContainingIgnoreCase(String category, String searchString, Pageable pageable);

    Page<Product> findByTagsContainingIgnoreCaseAndNameContainingIgnoreCase(String tags, String searchString,
            Pageable pageable);

    Page<Product> findByCategoryAndTagsContainingIgnoreCase(String category, String tags, Pageable pageable);

    Page<Product> findByCategory(String category, Pageable pageable);

    Page<Product> findByTagsContainingIgnoreCase(String tags, Pageable pageable);

    Optional<Product> findById(int id);

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchString,
            String searchString2, Pageable pageable);

}
