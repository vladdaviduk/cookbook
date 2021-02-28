package com.cookbook.service;

import com.cookbook.model.Recipe;

import java.util.List;

public interface RecipeService {

    List<Recipe> getRootRecipes();

    List<Recipe> getRelatedRecipes(long id);

    Recipe getRecipe(long id);

    Recipe getRecipe(long id, int version);

    Recipe createRecipe(Recipe recipe);

    Recipe updateRecipe(Recipe recipe, long id);
}
