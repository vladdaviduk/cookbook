package com.cookbook.service.impl;

import com.cookbook.exceptions.EntityNotFoundException;
import com.cookbook.model.Recipe;
import com.cookbook.repository.RecipeRepoWithPkGeneration;
import com.cookbook.repository.RecipeRepository;
import com.cookbook.service.RecipeService;
import com.cookbook.service.validation.RecipeValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeRepoWithPkGeneration repoWithIdGeneration;

    @Autowired
    private RecipeValidationService validationService;

    @Override
    public List<Recipe> getRootRecipes() {
        return recipeRepository.findLastVersionByParentIdIsNull();
    }

    @Override
    public List<Recipe> getRelatedRecipes(long id) {
        return recipeRepository.findByPk_Id(id);
    }

    @Override
    public Recipe getRecipe(long id) {
        Recipe recipe = getRawRecipe(id);
        List<Recipe> childRecipes = recipeRepository.findLastVersionByParentId(id);

        return buildRecipe(recipe, childRecipes);
    }

    @Override
    public Recipe getRecipe(long id, int version) {
        Recipe recipe = getRawRecipe(id, version);
        List<Recipe> childRecipes = recipeRepository.findLastVersionByParentId(id);

        return buildRecipe(recipe, childRecipes);
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        recipe.setCreated(new Date());
        recipe = repoWithIdGeneration.save(recipe);

        return buildRecipe(recipe, Collections.emptyList());
    }

    @Override
    public Recipe updateRecipe(Recipe recipe, long id) {
        Recipe recipeFromDb = getRawRecipe(id);
        validationService.validateUpdate(recipeFromDb, recipe);
        recipe.setCreated(recipeFromDb.getCreated());

        recipe = repoWithIdGeneration.save(recipe, id);
        List<Recipe> childRecipes = recipeRepository.findLastVersionByParentId(id);

        return buildRecipe(recipe, childRecipes);
    }

    private Recipe buildRecipe(Recipe recipe, List<Recipe> children) {
        recipe.getChildRecipes().addAll(children);
        recipe.setFullName(buildRecipeFullName(recipe));

        return recipe;
    }

    private String buildRecipeFullName(Recipe recipe) {

        StringBuilder fullName = new StringBuilder(recipe.getName());
        while (recipe.hasParent()) {
            recipe = getRawRecipe(recipe.getParentId());
            fullName.insert(0, recipe.getName() + " ");
        }

        return fullName.toString();
    }

    private Recipe getRawRecipe(long id) {
        return recipeRepository.findLastVersionById(id)
                .orElseThrow(() -> new EntityNotFoundException(Recipe.class, id));
    }

    private Recipe getRawRecipe(long id, int version) {
        return recipeRepository.findByPk_IdAndPk_Version(id, version)
                .orElseThrow(() -> new EntityNotFoundException(Recipe.class, id));
    }
}
