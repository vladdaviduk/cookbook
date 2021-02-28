package com.cookbook.service.validation;

import com.cookbook.model.Recipe;
import com.cookbook.model.dto.RecipeDto;

public interface RecipeValidationService {

    void validate(RecipeDto recipe);

    void validateUpdate(Recipe existingRecipe, Recipe newRecipe);
}
