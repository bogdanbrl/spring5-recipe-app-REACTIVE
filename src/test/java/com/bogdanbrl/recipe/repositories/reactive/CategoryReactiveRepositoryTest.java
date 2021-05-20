package com.bogdanbrl.recipe.repositories.reactive;

import com.bogdanbrl.recipe.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class CategoryReactiveRepositoryTest {

    @Autowired
    CategoryReactiveRepository categoryReactiveRepository;

    @BeforeEach
    public void setUp() {
        categoryReactiveRepository.deleteAll().block();
    }

    @Test
    public void findByDescription() {
        Category category = new Category();
        category.setDescription("foo");

        categoryReactiveRepository.save(category).then().block();

        Category fetchedCategory = categoryReactiveRepository.findByDescription("foo").block();
        assertNotNull(fetchedCategory.getId());

    }
}
