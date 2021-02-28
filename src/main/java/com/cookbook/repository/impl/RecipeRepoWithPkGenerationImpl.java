package com.cookbook.repository.impl;

import com.cookbook.model.Recipe;
import com.cookbook.model.pk.RecipePk;
import com.cookbook.repository.RecipePkGenerator;
import com.cookbook.repository.RecipeRepoWithPkGeneration;
import com.cookbook.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RecipeRepoWithPkGenerationImpl implements RecipeRepoWithPkGeneration {

    @Autowired
    private RecipePkGenerator pkGenerator;

    @Autowired
    private RecipeRepository repository;

    private static final int INITIAL_VERSION = 1;

    public Recipe save(Recipe recipe) {
        recipe.setPk(new RecipePk(pkGenerator.generateId(), INITIAL_VERSION));
        return repository.save(recipe);
    }

    public Recipe save(Recipe recipe, long id) {
        recipe.setPk(new RecipePk(id, pkGenerator.generateVersion(id)));
        return repository.save(recipe);
    }
}
