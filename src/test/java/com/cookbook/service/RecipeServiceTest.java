package com.cookbook.service;

import com.cookbook.exceptions.ValidationException;
import com.cookbook.model.Recipe;
import com.cookbook.model.pk.RecipePk;
import com.cookbook.repository.RecipeRepoWithPkGeneration;
import com.cookbook.repository.RecipeRepository;
import com.cookbook.service.impl.RecipeServiceImpl;
import com.cookbook.service.validation.impl.RecipeValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static com.cookbook.util.DateUtil.parseDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeServiceTest {

    @InjectMocks
    private RecipeServiceImpl recipeService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RecipeRepoWithPkGeneration repoWithIdGeneration;

    @Mock
    private RecipeValidationServiceImpl validationService;

    private static final int INITIAL_VERSION = 1;

    private static final long RECIPE_ID = 1L;
    private static final String RECIPE_NAME = "Fried Chicken";
    private static final Date RECIPE_CREATED_DATE = parseDate("2021-02-27 10:00:00");

    private static final long CHILD_RECIPE_ID = 2L;
    private static final String CHILD_RECIPE_NAME = "with Mayo";
    private static final Date CHILD_RECIPE_CREATED_DATE = parseDate("2021-02-27 11:00:00");

    private static final String NEW_RECIPE_NAME = "Hamburger";

    @Before
    public void init() {
        doCallRealMethod().when(validationService).validateUpdate(any(), any());
    }

    @Test
    public void testGetRecipe() {
        Recipe existingRecipe = persistentRecipeInstance(RECIPE_NAME, RECIPE_ID, INITIAL_VERSION);
        List<Recipe> recipeChildren = childrenInstance(RECIPE_ID);

        when(recipeRepository.findLastVersionById(RECIPE_ID)).thenReturn(Optional.of(existingRecipe));
        when(recipeRepository.findLastVersionByParentId(RECIPE_ID)).thenReturn(recipeChildren);

        Recipe recipe = recipeService.getRecipe(RECIPE_ID);

        assertEquals(existingRecipe, recipe);
        assertEquals(recipe.getName(), recipe.getFullName());

        sortRecipeLists(new ArrayList<>(List.of(recipeChildren, recipe.getChildRecipes())),
                Comparator.comparingLong((Recipe r) -> r.getPk().getId()));

        assertEquals(recipeChildren, recipe.getChildRecipes());
    }

    @Test
    public void testGetRecipeByVersion() {
        Recipe existingRecipe = persistentRecipeInstance(RECIPE_NAME, RECIPE_ID, INITIAL_VERSION);

        when(recipeRepository.findByPk_IdAndPk_Version(RECIPE_ID, INITIAL_VERSION))
                .thenReturn(Optional.of(existingRecipe));

        Recipe recipe = recipeService.getRecipe(RECIPE_ID, INITIAL_VERSION);

        assertEquals(existingRecipe, recipe);
    }

    @Test
    public void testGetRootRecipes() {
        List<Recipe> existingRootRecipes = new ArrayList<>(
                List.of(persistentRecipeInstance(RECIPE_NAME, RECIPE_ID, INITIAL_VERSION)));

        when(recipeRepository.findLastVersionByParentIdIsNull()).thenReturn(existingRootRecipes);

        List<Recipe> rootRecipes = recipeService.getRootRecipes();

        sortRecipeLists(new ArrayList<>(List.of(existingRootRecipes, rootRecipes)),
                Comparator.comparingLong((Recipe r) -> r.getPk().getId()));

        assertEquals(existingRootRecipes, rootRecipes);
    }

    @Test
    public void testGetRelatedRecipes() {
        List<Recipe> existingRelatedRecipes = new ArrayList<>(List.of(
                persistentRecipeInstance(RECIPE_NAME, RECIPE_ID, INITIAL_VERSION),
                persistentRecipeInstance(NEW_RECIPE_NAME, RECIPE_ID, INITIAL_VERSION + 1)));

        when(recipeRepository.findByPk_Id(RECIPE_ID)).thenReturn(existingRelatedRecipes);

        List<Recipe> relatedRecipes = recipeService.getRelatedRecipes(RECIPE_ID);

        sortRecipeLists(new ArrayList<>(List.of(existingRelatedRecipes, relatedRecipes)),
                Comparator.comparingInt((Recipe r) -> r.getPk().getVersion()));

        assertEquals(existingRelatedRecipes, relatedRecipes);
    }

    @Test
    public void testCreateRecipe() {
        Recipe transientRecipe = transientRecipeInstance(RECIPE_NAME);
        Recipe createdRecipe = persistentRecipeInstance(RECIPE_NAME, RECIPE_ID, INITIAL_VERSION);

        when(repoWithIdGeneration.save(transientRecipe)).thenReturn(createdRecipe);

        Recipe recipe = recipeService.createRecipe(transientRecipe);

        assertNotNull(transientRecipe.getCreated());
        assertEquals(createdRecipe, recipe);
    }

    @Test
    public void testUpdateRecipe() {
        int newVersion = INITIAL_VERSION + 1;

        Recipe existingRecipe = persistentRecipeInstance(RECIPE_NAME, RECIPE_ID, INITIAL_VERSION);
        Recipe transientRecipe = transientRecipeInstance(NEW_RECIPE_NAME);
        Recipe updatedRecipe = persistentRecipeInstance(NEW_RECIPE_NAME, RECIPE_ID, newVersion);

        when(recipeRepository.findLastVersionById(RECIPE_ID)).thenReturn(Optional.of(existingRecipe));
        when(repoWithIdGeneration.save(transientRecipe, RECIPE_ID)).thenReturn(updatedRecipe);

        Recipe recipe = recipeService.updateRecipe(transientRecipe, RECIPE_ID);

        assertNotNull(transientRecipe.getCreated());
        assertEquals(updatedRecipe, recipe);

        Recipe transientExistingRecipe = transientRecipeInstance(RECIPE_NAME);

        assertThrows(ValidationException.class, () -> recipeService.updateRecipe(transientExistingRecipe, RECIPE_ID));
    }

    private Recipe transientRecipeInstance(String name) {
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(name);

        return recipe;
    }

    private Recipe persistentRecipeInstance(String name, long id, int version) {
        Recipe recipe = transientRecipeInstance(name);
        recipe.setCreated(RECIPE_CREATED_DATE);
        recipe.setPk(new RecipePk(id, version));

        return recipe;
    }

    private List<Recipe> childrenInstance(long parentId) {
        Recipe recipe = new Recipe();
        recipe.setPk(new RecipePk(CHILD_RECIPE_ID, INITIAL_VERSION));
        recipe.setParentId(parentId);
        recipe.setName(CHILD_RECIPE_NAME);
        recipe.setDescription(CHILD_RECIPE_NAME);
        recipe.setCreated(CHILD_RECIPE_CREATED_DATE);

        return new ArrayList<>(List.of(recipe));
    }

    private void sortRecipeLists(List<List<Recipe>> listsOfRelatedRecipes, Comparator<Recipe> recipeComparator) {
        listsOfRelatedRecipes.forEach(relatedRecipes -> relatedRecipes.sort(recipeComparator));
    }
}
