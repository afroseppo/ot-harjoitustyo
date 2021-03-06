/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipebook.domain;

import java.util.HashMap;

/**
 * Class used represent recipes.
 * @author tiitinha
 */
public class Recipe implements Comparable<Recipe> {

    private String name;
    private HashMap<String, Ingredient> ingredients;
    private String author;

    /**
     * Sets the name and author for the recipe object.
     * @param name the name of the recipe
     * @param author name of the author of the recipe
     */
    public Recipe(String name, String author) {
        this.name = name.toLowerCase();
        this.ingredients = new HashMap<>();
        this.author = author;
    }

    /**
     * Method is used to add a new ingredient to a recipe.
     *
     * @param ingredient the ingredient instance to be added to the recipe
     * @return true if the ingredient doesn't exist yet, otherwise false
     */
    public boolean addIngredient(Ingredient ingredient) {
        if (ingredients.containsKey(ingredient.getName())) {
            return false;
        } else {
            ingredients.put(ingredient.getName(), ingredient);
            return true;
        }
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public int compareTo(Recipe t) {
        return this.name.hashCode() - t.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Recipe)) {
            return false;
        }

        Recipe other = (Recipe) obj;

        return name.equals(other.name);
    }

    public String getAuthor() {
        return author;
    }

}
