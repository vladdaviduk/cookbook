app.controller('MainController', function MainController($scope, recipeService){

    $scope.selectRecipe = function(recipe) {
        setRecipeData(recipeService.getRecipe(recipe.id))
    };

    $scope.selectRecipeVersion = function() {
        if (this.selectedVersion) {
            recipeService.getRecipe($scope.recipe.id, this.selectedVersion.version).then((value) => {
                $scope.recipe =  value.data;
            }, (reason) => {
                alert(reason.data.message)
            });
        }
    };

    $scope.addChild = function(recipe) {
        var parentId = recipe.id;
        $scope.recipe = getEmptyRecipe();
        $scope.recipe.parentId = parentId;
    };

    $scope.stepBack = function(recipe) {
        if (recipe.parentId)
            setRecipeData(recipeService.getRecipe(recipe.parentId))
        else
            $scope.refreshPage()
    };

    $scope.submitRecipe = function(recipe) {
        var promise = !recipe.id ? recipeService.createRecipe(recipe) : recipeService.updateRecipe(recipe);

        setRecipeData(promise)
    };

    $scope.refreshPage = function() {
        $scope.recipe = getEmptyRecipe()
        recipeService.getRootRecipes().then((value) => {
            $scope.recipes = value.data;
        }, (reason) => {
            alert(reason.data.message);
        });
    };

    let setRecipeData = function(promise) {
        promise.then((recipe) => {
            $scope.recipe =  recipe.data;
            $scope.recipes = $scope.recipe.childRecipes;
            recipeService.getRelatedRecipes($scope.recipe.id).then((relatedRecipes) => {
                $scope.relatedRecipes = relatedRecipes.data
                $scope.selectedVersion = this.selectedVersion = $scope.relatedRecipes
                    .filter(related => related.version === $scope.recipe.version)[0]
            }, (reason) => {
                alert(reason.data.message)
            });
        }, (reason) => {
            alert(reason.data.message)
        });
    }

    let getEmptyRecipe = function() {
        return {
            id: "",
            parentId: "",
            name: "",
            description: "",
            fullName: "",
            created: "",
            version: "",
            childRecipes: [],
            relatedRecipes: []
        };
    };

    $scope.refreshPage();
})