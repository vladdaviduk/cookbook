package com.cookbook.service.dto.impl;

import com.cookbook.model.Recipe;
import com.cookbook.service.dto.DtoService;
import com.cookbook.model.dto.RecipeDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.cookbook.util.DateUtil.fromDate;
import static com.cookbook.util.DateUtil.parseDate;

@Service
public class RecipeDtoService implements DtoService<Recipe, RecipeDto> {

    @Override
    public RecipeDto convertToDto(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getPk().getId());
        recipeDto.setParentId(recipe.getParentId());
        recipeDto.setName(recipe.getName());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setCreated(fromDate(recipe.getCreated()));
        recipeDto.setVersion(recipe.getPk().getVersion());
        recipeDto.setFullName(recipe.getFullName());

        if (recipe.hasChildren()) recipeDto.setChildRecipes(convertToDto(recipe.getChildRecipes()));

        return recipeDto;
    }

    @Override
    public List<RecipeDto> convertToDto(List<Recipe> recipes) {
        return recipes.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Recipe convertToDomain(RecipeDto recipeDto) {
        Recipe recipe = new Recipe();
        recipe.setParentId(recipeDto.getParentId());
        recipe.setName(recipeDto.getName());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCreated(parseDate(recipeDto.getCreated()));

        return recipe;
    }
}
