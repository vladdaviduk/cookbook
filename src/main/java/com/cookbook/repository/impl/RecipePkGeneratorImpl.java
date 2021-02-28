package com.cookbook.repository.impl;

import com.cookbook.repository.RecipePkGenerator;
import com.cookbook.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipePkGeneratorImpl implements RecipePkGenerator {

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public long generateId() {
        return recipeRepository.countDistinctId() + 1L;
    }

    @Override
    public int generateVersion(long recipeId) {
        return recipeRepository.countByPk_Id(recipeId) + 1;
    }

}
