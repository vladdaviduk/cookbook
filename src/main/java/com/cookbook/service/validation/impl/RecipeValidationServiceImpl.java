package com.cookbook.service.validation.impl;

import com.cookbook.exceptions.ValidationException;
import com.cookbook.model.Recipe;
import com.cookbook.model.dto.RecipeDto;
import com.cookbook.repository.RecipeRepository;
import com.cookbook.service.validation.RecipeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Strings.isNullOrEmpty;


@Service
public class RecipeValidationServiceImpl implements RecipeValidationService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    public void validate(RecipeDto recipe) {
        if (recipe.hasParent() && recipeRepository.findByPk_Id(recipe.getParentId()).isEmpty()) {
            throw new ValidationException("Invalid parent id provided");
        }
        if (isNullOrEmpty(recipe.getName())) {
            throw new ValidationException("Name of the recipe can't be empty");
        }
    }

    @Override
    public void validateUpdate(Recipe existingRecipe, Recipe newRecipe) {
        if (existingRecipe.getName().equals(newRecipe.getName()) &&
                existingRecipe.getDescription().equals(newRecipe.getDescription())) {
            throw new ValidationException("Nothing to change");
        }
    }
}
