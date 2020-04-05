/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipebook.Domain;

import recipebook.Dao.Database;
import recipebook.Dao.RecipeDao;
import recipebook.Dao.UserDao;

/**
 *
 * @author tiitinha
 */
public class RecipeBookService {

    private RecipeDao recipeDao;
    private UserDao userDao;
    private User loggedIn;

    public RecipeBookService(RecipeDao recipeDao, UserDao userDao) {
        this.recipeDao = recipeDao;
        this.userDao = userDao;
    }

    /**
     *
     * @param name the name of the new recipe
     * @return true, if creating the recipe succeeds, otherwise false
     */
    public boolean createNewRecipe(String name) {
        Recipe recipe = new Recipe(name);

        return true;
        /*
        try {

        } catch (Exception ex) {
            return false;
        }
        return true;*/
    }

    /**
     *
     * @param recipeName the name of the recipe to which the ingredient is added
     * @param name the name of the ingredient
     * @param amount the amount of the ingredient
     * @param unit the unit of the ingredient
     * @return false, if adding the ingredient doesn't succeed, otherwise true
     */
    public boolean addIngredient(String recipeName, String name, int amount, String unit) {

        try {
            return true;
            /*recipe.addIngredient(name, amount, unit);*/
        } catch (Exception ex) {
            return false;
        }

    }

    /**
     * Checks whether a database to store the data exists, if not, then creates
     * a new database
     *
     * @param path the path of the database file from the config file
     */
    public void checkIfDatabaseExists(String path) {

        Database database = new Database();

        if (!database.databaseExists(path)) {
            database.createDatabase(path);
        }

    }

    /**
     *
     * @param username username
     * @param password password
     * @return true, if the username exists in the database, false otherwise
     */
    public boolean login(String username, String password) {
        User user = userDao.findByUserName(username);

        if (user == null) {
            return false;
        }

        if (user.checkPassword(password)) {
            loggedIn = user;
            return true;
        }
        
        return false;
    }

    public User getLoggedUser() {
        return loggedIn;
    }

}