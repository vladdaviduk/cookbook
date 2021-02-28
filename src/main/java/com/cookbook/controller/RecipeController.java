package com.cookbook.controller;

import com.cookbook.model.Recipe;
import com.cookbook.model.dto.RecipeDto;
import com.cookbook.service.RecipeService;
import com.cookbook.service.dto.impl.RecipeDtoService;
import com.cookbook.service.validation.RecipeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeDtoService recipeDtoService;

    @Autowired
    private RecipeValidationService validationService;

    @GetMapping("/recipes")
    public List<RecipeDto> getRootRecipes() {
        return recipeDtoService.convertToDto(recipeService.getRootRecipes());
    }

    @GetMapping("/recipes/{id}")
    public RecipeDto getRecipe(@PathVariable long id, @RequestParam Optional<Integer> version) {
        Recipe recipe = version.isPresent() ? recipeService.getRecipe(id, version.get()) : recipeService.getRecipe(id);
        return recipeDtoService.convertToDto(recipe);
    }

    @GetMapping("/recipes/{id}/related")
    public List<RecipeDto> getRelatedRecipes(@PathVariable long id) {
        return recipeDtoService.convertToDto(recipeService.getRelatedRecipes(id));
    }

    @PostMapping("/recipes")
    public RecipeDto createRecipe(@RequestBody RecipeDto recipe) {
        validationService.validate(recipe);
        return recipeDtoService.convertToDto(recipeService.createRecipe(recipeDtoService.convertToDomain(recipe)));
    }

    @PutMapping("/recipes/{id}")
    public RecipeDto updateRecipe(@RequestBody RecipeDto recipe, @PathVariable long id) {
        validationService.validate(recipe);
        return recipeDtoService.convertToDto(recipeService.updateRecipe(recipeDtoService.convertToDomain(recipe), id));
    }
}
