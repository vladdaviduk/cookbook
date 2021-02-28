package com.cookbook.repository;

public interface RecipePkGenerator {

    long generateId();

    int generateVersion(long recipeId);
}
