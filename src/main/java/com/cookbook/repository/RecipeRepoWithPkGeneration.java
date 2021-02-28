package com.cookbook.repository;

import com.cookbook.model.Recipe;

public interface RecipeRepoWithPkGeneration {

    Recipe save(Recipe recipe);

    Recipe save(Recipe recipe, long id);
}
