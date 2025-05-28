package com.example.mart.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    public CategoryRepository categoryrRepository;

    @Autowired
    public CategoryItemRepository categoryItemRepository;

}
