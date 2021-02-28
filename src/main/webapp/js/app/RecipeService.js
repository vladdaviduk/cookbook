app.factory('recipeService', function RecipeService($http){

    const recipesPath = "/recipes/";

    return {
        getRootRecipes :function () {
            return $http.get(recipesPath);
        },

        getRelatedRecipes :function (id) {
            return $http.get(recipesPath + id + "/related");
        },

        getRecipe :function (id, version) {
            let params = version ? { version: version } : {}
            return $http.get(recipesPath + id, { params: params });
        },

        createRecipe :function (recipe) {
            return $http.post(recipesPath, recipe);
        },

        updateRecipe :function (recipe) {
            return $http.put(recipesPath + recipe.id, recipe);
        }
    }
})
