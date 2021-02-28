package com.cookbook.repository;

import com.cookbook.model.Recipe;
import com.cookbook.model.pk.RecipePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, RecipePk> {

    String LAST_VERSION_QUERY = "SELECT recipe.* FROM\n" +
            "(SELECT id, max(VERSION) VERSION FROM recipe GROUP BY id) last_recipe\n" +
            "  JOIN recipe ON recipe.id=last_recipe.id\n" +
            "  AND recipe.version=last_recipe.version\n";

    @Query(nativeQuery=true, value= LAST_VERSION_QUERY + "WHERE recipe.parent_id=  (:parentId);")
    List<Recipe> findLastVersionByParentId(long parentId);

    @Query(nativeQuery=true, value= LAST_VERSION_QUERY + "WHERE recipe.parent_id IS NULL;")
    List<Recipe> findLastVersionByParentIdIsNull();

    @Query(nativeQuery=true, value= LAST_VERSION_QUERY + "WHERE recipe.id= (:id);")
    Optional<Recipe> findLastVersionById(long id);

    Optional<Recipe> findByPk_IdAndPk_Version(long id, int version);

    List<Recipe> findByPk_Id(long id);

    int countByPk_Id(long id);

    @Query("select count(distinct r.pk.id) from Recipe r")
    long countDistinctId();
}
