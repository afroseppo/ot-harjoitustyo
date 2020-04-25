/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipebook.domain;

import recipebook.domain.User;
import recipebook.domain.Recipe;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import recipebook.dao.RecipeDao;

/**
 *
 * @author tiitinha
 */
public class FakeRecipeDao implements RecipeDao {
    
    List<Recipe> recipes;

    public FakeRecipeDao() {
        recipes = new ArrayList<>();
    }

    @Override
    public boolean addRecipe(Recipe recipe) throws Exception {
        recipes.add(recipe);
        return true;
    }

    @Override
    public Recipe fetchRecipe(String name) {
        return recipes.stream().filter(r -> r.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public List<Recipe> getAll() {
        return recipes;
    }

    @Override
    public List<Recipe> getUsersRecipes(User user) {
        return recipes.stream().filter(r -> r.getAuthor().equals(user.getName())).collect(Collectors.toList());
    }
    
}