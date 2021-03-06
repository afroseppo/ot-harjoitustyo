/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipebook.domain;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiitinha
 */
public class RecipeTest {

    private Recipe recipe;

    @Before
    public void setUp() {
        recipe = new Recipe("omlette", "test");
    }

    @Test
    public void addingNewIngredientReturnsTrue() {
        Ingredient ingredient = new Ingredient("egg", 1, "pcs");
        assertTrue(recipe.addIngredient(ingredient));
    }

    @Test
    public void addingDuplicateIngredientReturnsFalse() {
        Ingredient ingredient = new Ingredient("egg", 1, "pcs");
        Ingredient ingredient2 = new Ingredient("egg", 1, "pcs");

        recipe.addIngredient(ingredient);
        assertFalse(recipe.addIngredient(ingredient2));
    }

    @Test
    public void addingIgredientAddsIngredientToIngredientList() {
        Ingredient ingredient = new Ingredient("egg", 1, "pcs");

        recipe.addIngredient(ingredient);
        assertTrue(recipe.getIngredients().containsKey("egg"));
    }

    @Test
    public void equalsReturnsTrueWithSameRecipeName() {
        Recipe newRecipe = new Recipe("omlette", "test");
        assertTrue(recipe.equals(newRecipe));
    }

    @Test
    public void compareToReturnsZeroIfSameName() {
        Recipe recipe2 = new Recipe("omlette", "test");
        assertTrue(recipe.compareTo(recipe2) == 0);
    }

    @Test
    public void compareToDoesntReturnZeroIfDifferentName() {
        Recipe recipe2 = new Recipe("egglette", "test");
        assertFalse(recipe.compareTo(recipe2) == 0);
    }
}
